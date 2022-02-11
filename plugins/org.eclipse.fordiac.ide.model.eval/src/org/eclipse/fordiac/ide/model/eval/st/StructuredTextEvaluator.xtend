/*******************************************************************************
 * Copyright (c) 2022 Martin Erich Jobst
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Martin Jobst - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.model.eval.st

import java.util.Collection
import java.util.List
import java.util.Map
import org.eclipse.fordiac.ide.model.data.DataType
import org.eclipse.fordiac.ide.model.eval.AbstractEvaluator
import org.eclipse.fordiac.ide.model.eval.Evaluator
import org.eclipse.fordiac.ide.model.eval.EvaluatorExitException
import org.eclipse.fordiac.ide.model.eval.value.BoolValue
import org.eclipse.fordiac.ide.model.eval.value.Value
import org.eclipse.fordiac.ide.model.eval.variable.ElementaryVariable
import org.eclipse.fordiac.ide.model.eval.variable.Variable
import org.eclipse.fordiac.ide.model.libraryElement.BaseFBType
import org.eclipse.fordiac.ide.model.libraryElement.STAlgorithm
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration
import org.eclipse.fordiac.ide.structuredtextalgorithm.sTAlgorithm.STAlgorithmBody
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.ArrayInitializerExpression
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STAssignmentStatement
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STBinaryExpression
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STCaseStatement
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STContinue
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STExit
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STExpression
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STFeatureExpression
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STForStatement
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STIfStatement
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STNumericLiteral
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STRepeatStatement
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STReturn
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STStatement
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STUnaryExpression
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STVarDeclaration
import org.eclipse.fordiac.ide.structuredtextcore.sTCore.STWhileStatement
import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtext.parser.IParseResult

import static extension org.eclipse.emf.ecore.util.EcoreUtil.getRootContainer
import static extension org.eclipse.fordiac.ide.model.eval.value.ValueOperations.*
import static extension org.eclipse.fordiac.ide.structuredtextalgorithm.util.StructuredTextParseUtil.*

class StructuredTextEvaluator extends AbstractEvaluator {

	@Accessors final String name
	final String text
	final BaseFBType fbType
	final boolean singleExpression
	final Map<String, Variable> variables

	new(STAlgorithm alg, Collection<Variable> variables, Evaluator parent) {
		super(parent)
		val root = alg.rootContainer
		this.fbType = if(root instanceof BaseFBType) root else null
		this.name = '''«IF fbType !== null»«fbType.name».«ENDIF»«alg.name»'''
		this.text = alg.text
		this.singleExpression = false
		this.variables = variables.toMap[getName]
	}

	new(String text, Collection<Variable> variables, BaseFBType fbType, Evaluator parent) {
		super(parent)
		this.name = "anonymous"
		this.text = text
		this.fbType = fbType
		this.singleExpression = true
		this.variables = variables.toMap[getName]
	}

	override getVariables() {
		variables.unmodifiableView
	}

	override getSourceElement() {
		this.fbType
	}

	override Value evaluate() {
		val parseResult = parse()
		val root = parseResult.rootASTElement
		root.evaluate
	}

	def private dispatch Value evaluate(STAlgorithmBody alg) {
		alg.trap.evaluateStructuredTextAlgorithm
		null
	}

	def private dispatch Value evaluate(STExpression expr) {
		expr.trap.evaluateExpression
	}

	def private IParseResult parse() {
		val errors = newArrayList
		val parseResult = text.parse(singleExpression, name, fbType, errors)
		if (parseResult === null) {
			errors.forEach[error("Parse error: " + it)]
			throw new Exception("Parse error: " + errors.join(", "))
		}
		return parseResult
	}

	def private evaluateStructuredTextAlgorithm(STAlgorithmBody alg) {
		alg.varTempDeclarations.flatMap[varDeclarations].filter(STVarDeclaration).forEach[evaluateLocalVariable]
		try {
			alg.statements.evaluateStatementList
		} catch (ReturnException e) {
			// return
		}
	}

	def private void evaluateLocalVariable(STVarDeclaration variable) {
		variables.put(variable.name,
			new ElementaryVariable(variable.name, variable.type as DataType,
				variable.defaultValue?.evaluateInitializerExpression))
	}

	def private dispatch Value evaluateInitializerExpression(STExpression expression) {
		expression.evaluateExpression
	}

	def private dispatch Value evaluateInitializerExpression(ArrayInitializerExpression expression) {
		throw new UnsupportedOperationException
	}

	def private void evaluateStatementList(List<STStatement> statements) {
		statements.forEach[evaluateStatement]
	}

	def private dispatch void evaluateStatement(STStatement stmt) {
		error('''The statement «stmt.eClass.name» is not supported''')
		throw new UnsupportedOperationException('''The statement «stmt.eClass.name» is not supported''')
	}

	def private dispatch void evaluateStatement(STAssignmentStatement stmt) {
		stmt.left.evaluateVariable.value = stmt.right.trap.evaluateExpression
	}

	def private dispatch void evaluateStatement(STIfStatement stmt) {
		if (stmt.condition.trap.evaluateExpression.asBoolean) {
			stmt.statements.evaluateStatementList
		} else {
			(stmt.elseifs.findFirst[condition.trap.evaluateExpression.asBoolean]?.statements ?:
				stmt.^else?.statements)?.evaluateStatementList
		}
	}

	def private dispatch void evaluateStatement(STCaseStatement stmt) {
		val value = stmt.selector.trap.evaluateExpression;
		(stmt.cases.findFirst[conditions.exists[trap.evaluateExpression == value]]?.statements ?: stmt.^else?.statements)?.
			evaluateStatementList
	}

	def private dispatch void evaluateStatement(STForStatement stmt) {
		val variable = variables.get(stmt.variable.name)
		// from
		variable.value = stmt.from.trap.evaluateExpression
		// to
		val to = stmt.to.evaluateExpression
		// by
		val by = stmt.by?.evaluateExpression ?: 1.wrapValue(variable.type)
		// direction?
		if (by >= variable.type.defaultValue) {
			while (variable.value <= to) {
				try {
					stmt.statements.evaluateStatementList
				} catch (ContinueException e) {
					// continue
				}
				(stmt.by ?: stmt.from).trap
				variable.value = variable.value + by
			}
		} else {
			while (variable.value >= to) {
				try {
					stmt.statements.evaluateStatementList
				} catch (ContinueException e) {
					// continue
				}
				stmt.by.trap
				variable.value = variable.value + by
			}
		}
	}

	def private dispatch void evaluateStatement(STWhileStatement stmt) {
		while (stmt.condition.trap.evaluateExpression.asBoolean) {
			try {
				stmt.statements.evaluateStatementList
			} catch (ContinueException e) {
				// continue
			}
		}
	}

	def private dispatch void evaluateStatement(STRepeatStatement stmt) {
		do {
			try {
				stmt.statements.evaluateStatementList
			} catch (ContinueException e) {
				// continue
			}
		} while (!stmt.condition.trap.evaluateExpression.asBoolean);
	}

	def private dispatch void evaluateStatement(STContinue stmt) { throw new ContinueException(stmt.trap) }

	def private dispatch void evaluateStatement(STReturn stmt) { throw new ReturnException(stmt.trap) }

	def private dispatch void evaluateStatement(STExit stmt) {
		throw new StructuredTextExitException(stmt.trap, this)
	}

	def private dispatch Value evaluateExpression(STExpression expr) {
		error('''The expression «expr.eClass.name» is not supported''')
		throw new UnsupportedOperationException('''The expression «expr.eClass.name» is not supported''')
	}

	def private dispatch Value evaluateExpression(STBinaryExpression expr) {
		switch (expr.op) {
			case ADD:
				expr.left.evaluateExpression + expr.right.evaluateExpression
			case SUB:
				expr.left.evaluateExpression - expr.right.evaluateExpression
			case MUL:
				expr.left.evaluateExpression * expr.right.evaluateExpression
			case DIV:
				expr.left.evaluateExpression / expr.right.evaluateExpression
			case MOD:
				expr.left.evaluateExpression % expr.right.evaluateExpression
			case POWER:
				expr.left.evaluateExpression ** expr.right.evaluateExpression
			case AND,
			case AMPERSAND:
				switch (left: expr.left.evaluateExpression) {
					BoolValue case left.boolValue: expr.right.evaluateExpression
					BoolValue: BoolValue.FALSE
					default: left.bitwiseAnd(expr.right.evaluateExpression)
				}
			case OR:
				switch (left: expr.left.evaluateExpression) {
					BoolValue case !left.boolValue: expr.right.evaluateExpression
					BoolValue: BoolValue.TRUE
					default: left.bitwiseOr(expr.right.evaluateExpression)
				}
			case XOR:
				switch (left: expr.left.evaluateExpression) {
					BoolValue: BoolValue.toBoolValue(left.boolValue.xor(expr.right.evaluateExpression.asBoolean))
					default: left.bitwiseXor(expr.right.evaluateExpression)
				}
			case EQ:
				BoolValue.toBoolValue(expr.left.evaluateExpression == expr.right.evaluateExpression)
			case NE:
				BoolValue.toBoolValue(expr.left.evaluateExpression != expr.right.evaluateExpression)
			case LT:
				BoolValue.toBoolValue(expr.left.evaluateExpression < expr.right.evaluateExpression)
			case LE:
				BoolValue.toBoolValue(expr.left.evaluateExpression <= expr.right.evaluateExpression)
			case GT:
				BoolValue.toBoolValue(expr.left.evaluateExpression > expr.right.evaluateExpression)
			case GE:
				BoolValue.toBoolValue(expr.left.evaluateExpression >= expr.right.evaluateExpression)
			default: {
				error('''The operator «expr.op» is not supported''')
				throw new UnsupportedOperationException('''The operator «expr.op» is not supported''')
			}
		}
	}

	def private dispatch Value evaluateExpression(STUnaryExpression expr) {
		switch (expr.op) {
			case PLUS:
				+expr.expression.evaluateExpression
			case MINUS:
				-expr.expression.evaluateExpression
			case NOT:
				switch (value: expr.expression.evaluateExpression) {
					BoolValue: BoolValue.toBoolValue(!value.boolValue)
					default: value.bitwiseNot
				}
			default: {
				error('''The operator «expr.op» is not supported''')
				throw new UnsupportedOperationException('''The operator «expr.op» is not supported''')
			}
		}
	}

	def private dispatch Value evaluateExpression(STNumericLiteral expr) {
		expr.value.wrapValue(expr.type)
	}

	def private dispatch Value evaluateExpression(STFeatureExpression expr) {
		switch (feature: expr.feature) {
			VarDeclaration:
				variables.get(feature.name).value
			STVarDeclaration:
				variables.get(feature.name).value
			default: {
				error('''The feature «feature.eClass.name» is not supported''')
				throw new UnsupportedOperationException('''The feature «feature.eClass.name» is not supported''')
			}
		}
	}

	def private dispatch Variable evaluateVariable(STExpression expr) {
		error('''The lvalue expression «expr.eClass.name» is not supported''')
		throw new UnsupportedOperationException('''The lvalue expression «expr.eClass.name» is not supported''')
	}

	def private dispatch Variable evaluateVariable(STFeatureExpression expr) {
		switch (feature: expr.feature) {
			VarDeclaration:
				variables.get(feature.name)
			STVarDeclaration:
				variables.get(feature.name)
			default: {
				error('''The feature «feature.eClass.name» is not supported''')
				throw new UnsupportedOperationException('''The feature «feature.eClass.name» is not supported''')
			}
		}
	}

	static class StructuredTextException extends Exception {
		new(STStatement statement) {
			super(statement.eClass.name)
		}
	}

	static class ContinueException extends StructuredTextException {
		new(STStatement statement) {
			super(statement)
		}
	}

	static class ReturnException extends StructuredTextException {
		new(STStatement statement) {
			super(statement)
		}
	}

	static class StructuredTextExitException extends EvaluatorExitException {
		new(STStatement statement, Evaluator evaluator) {
			super(evaluator)
		}
	}
}
