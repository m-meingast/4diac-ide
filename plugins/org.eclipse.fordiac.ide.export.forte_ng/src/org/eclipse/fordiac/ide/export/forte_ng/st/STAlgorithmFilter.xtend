/*******************************************************************************
 * Copyright (c) 2019 fortiss GmbH
 *               2020 Johannes Kepler University Linz
 * 				 2021 Primetals Technologies Austria GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Martin Jobst - initial API and implementation and/or initial documentation
 *   Ernst Blecha - add multibit partial access
 *   Martin Melik Merkumians - fixes partial access and changes type resolution from
 * 		String information to object comparison
 *******************************************************************************/

package org.eclipse.fordiac.ide.export.forte_ng.st

import java.text.MessageFormat
import java.util.ArrayList
import java.util.List
import org.eclipse.emf.common.util.BasicEList
import org.eclipse.emf.common.util.EList
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.ResourceSet
import org.eclipse.fordiac.ide.export.forte_ng.ForteLibraryElementTemplate
import org.eclipse.fordiac.ide.model.FordiacKeywords
import org.eclipse.fordiac.ide.model.data.DataType
import org.eclipse.fordiac.ide.model.data.StructuredType
import org.eclipse.fordiac.ide.model.datatype.helper.IecTypes.ElementaryTypes
import org.eclipse.fordiac.ide.model.libraryElement.AdapterDeclaration
import org.eclipse.fordiac.ide.model.libraryElement.AdapterFBType
import org.eclipse.fordiac.ide.model.libraryElement.BaseFBType
import org.eclipse.fordiac.ide.model.libraryElement.BasicFBType
import org.eclipse.fordiac.ide.model.libraryElement.FB
import org.eclipse.fordiac.ide.model.libraryElement.FBType
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement
import org.eclipse.fordiac.ide.model.libraryElement.InterfaceList
import org.eclipse.fordiac.ide.model.libraryElement.STAlgorithm
import org.eclipse.fordiac.ide.model.libraryElement.SimpleFBType
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration
import org.eclipse.fordiac.ide.model.structuredtext.parser.antlr.StructuredTextParser
import org.eclipse.fordiac.ide.model.structuredtext.resource.StructuredTextResource
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.AdapterRoot
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.AdapterVariable
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.ArrayVariable
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.AssignmentStatement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryExpression
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.BinaryOperator
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.BoolLiteral
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.Call
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.CaseClause
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.CaseStatement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.ContinueStatement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.ExitStatement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.Expression
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.FBCall
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.ForStatement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.IfStatement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.InArgument
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.IntLiteral
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.LocalVariable
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.OutArgument
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.PartialAccess
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.PrimaryVariable
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.RealLiteral
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.RepeatStatement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.ReturnStatement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.Statement
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.StatementList
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.StringLiteral
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextAlgorithm
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.TimeLiteral
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.UnaryExpression
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.UnaryOperator
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.WhileStatement
import org.eclipse.fordiac.ide.model.structuredtext.validation.DatetimeLiteral
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.util.CancelIndicator
import org.eclipse.xtext.util.LazyStringInputStream
import org.eclipse.xtext.validation.CheckMode

import static extension org.eclipse.emf.ecore.util.EcoreUtil.copy
import static extension org.eclipse.emf.ecore.util.EcoreUtil.getRootContainer
import static extension org.eclipse.xtext.util.Strings.convertToJavaString

class STAlgorithmFilter {

	static final String SYNTHETIC_URI_NAME = "__synthetic" // $NON-NLS-1$
	static final String URI_SEPERATOR = "." // $NON-NLS-1$
	static final String FB_URI_EXTENSION = "xtextfbt" // $NON-NLS-1$
	static final String ST_URI_EXTENSION = "st" // $NON-NLS-1$
	static final CharSequence EXPORT_PREFIX = ForteLibraryElementTemplate.EXPORT_PREFIX

	static final IResourceServiceProvider SERVICE_PROVIDER = IResourceServiceProvider.Registry.INSTANCE.
		getResourceServiceProvider(URI.createURI(SYNTHETIC_URI_NAME + URI_SEPERATOR + ST_URI_EXTENSION))

	def createFBResource(XtextResourceSet resourceSet, BaseFBType fbType) {
		// create resource for function block and add copy
		val fbResource = resourceSet.createResource(resourceSet.computeUnusedUri(FB_URI_EXTENSION))
		fbResource.contents.add(fbType)
		fbType.interfaceList.sockets.forEach[adp|createAdapterResource(resourceSet, adp)];
		fbType.interfaceList.plugs.forEach[adp|createAdapterResource(resourceSet, adp)];
		fbType.interfaceList.inputVars.forEach[v|createStructResource(resourceSet, v)];
		fbType.interfaceList.outputVars.forEach[v|createStructResource(resourceSet, v)];
		fbType.internalVars.forEach[v|createStructResource(resourceSet, v)];
	}

	def void createAdapterResource(XtextResourceSet resourceSet, AdapterDeclaration adapter) {
		val adapterResource = resourceSet.createResource(resourceSet.computeUnusedUri(FB_URI_EXTENSION));
		adapterResource.contents.add(adapter.type.adapterFBType);
	}

	def void createStructResource(XtextResourceSet resourceSet, VarDeclaration variable) {
		if (variable.type instanceof StructuredType) {
			val structResource = resourceSet.createResource(resourceSet.computeUnusedUri(FB_URI_EXTENSION));
			val type = variable.type as StructuredType;
			structResource.contents.add(type);
			type.memberVariables.forEach[v|createStructResource(resourceSet, v)];
		}
	}

	def protected URI computeUnusedUri(ResourceSet resourceSet, String fileExtension) {
		for (i : 0 ..< Integer.MAX_VALUE) {
			val syntheticUri = URI.createURI(SYNTHETIC_URI_NAME + i + URI_SEPERATOR + fileExtension) // $NON-NLS-1$
			if (resourceSet.getResource(syntheticUri, false) === null) {
				return syntheticUri
			}
		}
		throw new IllegalStateException()
	}

	def parseAlgorithm(STAlgorithm alg) {
		val resourceSet = SERVICE_PROVIDER.get(ResourceSet) as XtextResourceSet
		createFBResource(resourceSet, alg.rootContainer as BaseFBType)
		// create resource for algorithm
		val resource = resourceSet.createResource(resourceSet.computeUnusedUri(ST_URI_EXTENSION)) as XtextResource
		resource.load(new LazyStringInputStream(alg.text), #{XtextResource.OPTION_RESOLVE_ALL -> Boolean.TRUE})
		val stalg = resource.parseResult.rootASTElement as StructuredTextAlgorithm
		stalg.localVariables.forEach[v|createStructResource(resourceSet, v)]
		return resource
	}

	def generateLocalVariables(STAlgorithm alg) {
		val parseResult = alg.parseAlgorithm.parseResult
		val stalg = parseResult.rootASTElement as StructuredTextAlgorithm
		stalg.localVariables.forEach[v|v.typeName = v.type.name]
		return stalg.localVariables
	}

	def generate(STAlgorithm alg, List<String> errors) {
		val resource = alg.parseAlgorithm
		val parseResult = resource.parseResult
		val validator = resource.resourceServiceProvider.resourceValidator
		val issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl)
		if (!issues.empty) {
			errors.addAll(issues.map [
				MessageFormat.format("{0}, Line {1}: {2}", alg.name, Long.toString(it.lineNumber), it.message)
			])
			return null
		}
		val stalg = parseResult.rootASTElement as StructuredTextAlgorithm
		stalg.generateStructuredTextAlgorithm
	}

	def generate(String expression, BasicFBType fb, List<String> errors) {
		val resourceSet = SERVICE_PROVIDER.get(ResourceSet) as XtextResourceSet
		createFBResource(resourceSet, fb.copy as BaseFBType)
		// create resource for algorithm
		val resource = resourceSet.createResource(resourceSet.computeUnusedUri(ST_URI_EXTENSION)) as XtextResource
		val parser = resource.parser as StructuredTextParser
		resource.load(new LazyStringInputStream(expression),
			#{StructuredTextResource.OPTION_PARSER_RULE -> parser.grammarAccess.expressionRule})
		val parseResult = resource.parseResult
		val validator = resource.resourceServiceProvider.resourceValidator
		val issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl)
		if (!issues.empty) {
			errors.addAll(issues.map[it.message])
			return null
		}
		val expr = parseResult.rootASTElement as Expression
		expr.generateExpression
	}

	def protected generateStructuredTextAlgorithm(StructuredTextAlgorithm alg) '''
		«alg.localVariables.generateLocalVariables»
		«alg.statements.generateStatementList»
	'''
	
	def private dispatch int BitSize(PartialAccess part) {
		if(part !== null) {
			if(part.bitaccess)			ElementaryTypes.BOOL.BitSize
			else if(part.byteaccess)	ElementaryTypes.BYTE.BitSize
			else if(part.wordaccess)	ElementaryTypes.WORD.BitSize
			else if(part.dwordaccess)	ElementaryTypes.DWORD.BitSize
			else					    0
		} else 0
	}
	
	def private dispatch int BitSize(PrimaryVariable variable) {
		variable.^var.type.BitSize
	}
	
	def private dispatch int BitSize(AdapterVariable variable) {
		variable.^var.type.BitSize
	}
	
	def private dispatch int BitSize(VarDeclaration declaration) {
		declaration.type.BitSize
	}
	
	def private dispatch int BitSize(DataType type) {
		switch(type) {
			case ElementaryTypes.LWORD: 64
			case ElementaryTypes.DWORD: 32
			case ElementaryTypes.WORD: 16
			case ElementaryTypes.BYTE: 8
			case ElementaryTypes.BOOL: 1
			default: 0
		}
	}

	def private dispatch int BitSize(CharSequence str) {
		switch str {
			case FordiacKeywords.LWORD: 64
			case FordiacKeywords.DWORD: 32
			case FordiacKeywords.WORD: 16
			case FordiacKeywords.BYTE: 8
			case FordiacKeywords.BOOL: 1
			default: 0
		}
	}

	def protected generateArrayDecl(LocalVariable variable) '''«IF variable.located»
			«variable.generateArrayDeclLocated»«ELSE»«variable.generateArrayDeclLocal»«ENDIF»
	'''

	def protected generateArrayDeclLocated(LocalVariable variable) {
		val l = variable.location
		switch l {
			PrimaryVariable: '''
				«IF variable.type.name.BitSize > 0 && l.^var.type.name.BitSize > 0»
					«IF l.^var.type.name.BitSize > variable.type.name.BitSize»
						ARRAY_AT<CIEC_«variable.type.name», CIEC_«l.^var.type.name», 0, «variable.arraySize-1»> «variable.generateVarAccessLocal»(«l.^var.generateVarAccess»);
					«ELSE»
						#error Accessing CIEC_«l.^var.type.name» via CIEC_«variable.type.name» would result in undefined behaviour
					«ENDIF»
				«ELSE»
					#error Piecewise access is supported only for types with defined bit-representation (e.g. not CIEC_«l.^var.type.name» via CIEC_«variable.type.name»)
				«ENDIF»
			'''
			default: '''#error unhandled located array'''
		}
	}

	def protected generateArrayDeclLocal(LocalVariable variable) '''
		CIEC_«variable.type.name» «EXPORT_PREFIX»«variable.name»[«variable.arraySize»]«variable.generateLocalVariableInitializer»;
	'''

	def protected generateVariableDeclLocated(LocalVariable variable) {
		val l = variable.location
		switch l {
			PrimaryVariable: '''// replacing all instances of «variable.extractTypeInformation»:«variable.generateVarAccessLocal» with «variable.generateVarAccess»''' // names will just be replaced during export
			default: '''#error located variable of unhandled type'''
		}
	}

	def protected generateVariableDeclLocal(LocalVariable variable) '''
		CIEC_«variable.type.name» «variable.generateVarAccessLocal»«variable.generateLocalVariableInitializer»;
	'''

	def protected generateLocalVariables(List<VarDeclaration> variables) '''
		«FOR variable : variables»
			«switch (variable) {
				LocalVariable case !variable.located && !variable.array             : variable.generateVariableDeclLocal
				LocalVariable case !variable.located &&  variable.array             : variable.generateArrayDeclLocal
				LocalVariable case variable.located && null !== variable.location && !variable.array : variable.generateVariableDeclLocated
				LocalVariable case variable.located && null !== variable.location &&  variable.array : variable.generateArrayDeclLocated
			}»
		«ENDFOR»
	'''

	def protected generateLocalVariableInitializer(VarDeclaration variable) {
		switch (variable) {
			LocalVariable case variable.initialValue !== null: ''' = «variable.initialValue.generateExpression»'''
			default:
				""
		}
	}

	def protected CharSequence generateStatementList(StatementList list) '''
		«FOR stmt : list.statements»
			«stmt.generateStatement»
		«ENDFOR»
	'''

	def protected dispatch generateStatement(Statement stmt) {
		throw new UnsupportedOperationException(stmt.eClass + " not supported");
	}

	def protected dispatch generateStatement(AssignmentStatement stmt) '''
		«stmt.variable.generateExpression» = «stmt.expression.generateExpression»;
	'''

	def protected dispatch generateStatement(Call stmt) {
		return stmt.generateExpression
	}

	def protected dispatch generateStatement(FBCall fbCall) '''
		«generateInAssignments(fbCall)»
		mInternalFBs[«internalFbIndexFromName(fbCall.fb)»]->receiveInputEvent(«eventIndexFromName(fbCall)», nullptr);
		«generateOutAssignments(fbCall)»
	'''

	def generateInAssignments(FBCall call) {
		val inArgs = call.args.filter(InArgument)
		'''
			«FOR inArg : inArgs»
				«generateInAssigmentExpression(call.fb, inArg)» = «generateInAssigmentRHS(inArg)»;
			«ENDFOR»
		'''
	}

	def generateInAssigmentRHS(InArgument inArg) {
		val inArgRHS = inArg.expr
		if (inArgRHS instanceof PrimaryVariable && (inArgRHS as PrimaryVariable).^var.array) {
			val inArgVar = (inArgRHS as PrimaryVariable).^var
			if((inArgRHS as PrimaryVariable).^var.eContainer instanceof InterfaceList) {
				val argFBInterfaceList = (inArgRHS as PrimaryVariable).^var.eContainer as InterfaceList
				'''*static_cast<CIEC_ARRAY*>(getDI(«getInputIndex(argFBInterfaceList, inArgVar.name)»))'''				
			} else if ((inArgRHS as PrimaryVariable).^var.eContainer instanceof SimpleFBType) {
				val fbType = ((inArgRHS as PrimaryVariable).^var.eContainer as SimpleFBType)
				'''*static_cast<CIEC_ARRAY*>(«generateGetVariable(fbType, inArgVar.name)»)'''
			} else if ((inArgRHS as PrimaryVariable).^var.eContainer instanceof StructuredTextAlgorithm) {
				inArg.expr.generateExpression
			}
		} else {
			return inArg.expr.generateExpression
		}
	}

	def generateInAssigmentExpression(FB calledFb, InArgument argument) {
		val varDec = getInputVarDeclaration(calledFb, argument)
		if (varDec.array) {
			return '''*static_cast<CIEC_ARRAY*>(mInternalFBs[«internalFbIndexFromName(calledFb)»]->getDI(«getInputIndex(calledFb, argument.^var)»))'''
		} else {
			return '''*static_cast<CIEC_«varDec.type.name»*>(mInternalFBs[«internalFbIndexFromName(calledFb)»]->getDI(«getInputIndex(calledFb, argument.^var)»))'''
		}
	}

	def getInputVarDeclaration(FB fb, InArgument argument) {
		fb.interface.inputVars.findFirst[it.name == argument.^var]
	}

	def getInterfaceElementIndex(EList<? extends IInterfaceElement> interfaceList, String elementName) {
		var index = 0
		for (interfaceElement : interfaceList) {
			if (interfaceElement.name == elementName) {
				return index
			}
			index++
		}
		return null
	}

	def getInputIndex(InterfaceList list, String varName) {
		getInterfaceElementIndex(list.inputVars, varName)
	}

	def getInputIndex(FBType fbType, String varName) {
		getInputIndex(fbType.interfaceList, varName)
	}

	def getInputIndex(FB fb, String varName) {
		getInputIndex(fb.interface, varName)
	}

	def generateOutAssignments(FBCall call) {
		val outArgs = call.args.filter(OutArgument)
		'''
			«FOR outArg : outArgs»
				«generateOutAssignmentLHS(outArg)» = «generateOutAssignmentRHS(call, outArg)»;
			«ENDFOR»
		'''
	}

	def generateOutAssignmentRHS(FBCall fbCall, OutArgument argument) {
		val fbType = fbCall.fb.type
		val varDec = getTargetVarDeclaration(fbType, argument)
		if(varDec !== null) {
			if (varDec.array) {
				'''*static_cast<CIEC_ARRAY*>(mInternalFBs[«internalFbIndexFromName(fbCall.fb)»]->«generateGetVariable(fbType, varDec.name)»)'''
			} else {
				'''*static_cast<CIEC_«varDec.type.name»*>(mInternalFBs[«internalFbIndexFromName(fbCall.fb)»]->«generateGetVariable(fbType, varDec.name)»)'''
			}
		}
	}

	def generateGetVariable(FBType type, String name) {
		val inputIndex = getInputIndex(type, name)
		if (inputIndex !== null) {
			return '''getDI(«inputIndex»)'''
		}

		val outputIndex = getOutputIndex(type, name)
		if (outputIndex !== null) {
			return '''getDO(«outputIndex»)'''
		}

		val internalVariableIndex = type instanceof BaseFBType ? getInternalVarIndex(type, name) : null;
		if (internalVariableIndex !== null) {
			return '''getVarInternal(«getInternalVarIndex(type as BaseFBType, name)»)'''
		}
		throw new IllegalArgumentException("Name " + name + " not a variable on FB type " + type.name);
	}

	def generateOutAssignmentLHS(OutArgument argument) {
		val goalExpression = argument.expr
		if (goalExpression instanceof PrimaryVariable) {
			val outVariable = goalExpression.^var
			val callingFBType = outVariable.eContainer.eContainer as FBType
			if (outVariable.array) {
				return '''*static_cast<CIEC_ARRAY*>(«generateGetVariable(callingFBType, outVariable.name)»)'''
			} else {
				'''«argument.expr.generateExpression»'''
			}
		}
	}

	def getVarDeclaration(EList<? extends IInterfaceElement> interfaceList, String varName) {
		interfaceList.findFirst[it.name == varName] as VarDeclaration
	}

	def getTargetVarDeclaration(FB fb, OutArgument argument) {
		getTargetVarDeclaration(fb.type, argument)
	}

	def getTargetVarDeclaration(FBType fbType, OutArgument argument) {
		val varName = argument.^var
		getVarDeclaration(fbType.interfaceList.outputVars, varName) ||
			getVarDeclaration(fbType.interfaceList.inputVars, varName) ||
			(fbType instanceof BaseFBType ? getVarDeclaration(fbType.internalVars, varName) : null)
	}

	def operator_or(VarDeclaration element, VarDeclaration element2) {
		return element !== null ? element : element2
	}

	def getOutputIndex(FB fb, String varName) {
		getInterfaceElementIndex(fb.interface.outputVars, varName)
	}

	def getOutputIndex(FBType fbType, String varName) {
		getInterfaceElementIndex(fbType.interfaceList.outputVars, varName)
	}

	def getInternalVarIndex(FB fb, String varName) {
		if (fb.type instanceof BaseFBType) {
			getInternalVarIndex(fb.type as BaseFBType, varName)
		}
		return null
	}

	def getInternalVarIndex(BaseFBType fbType, String varName) {
		getInterfaceElementIndex(fbType.internalVars, varName)
	}

	def eventIndexFromName(FBCall fbCall) {
		getInterfaceElementIndex(fbCall.fb.interface.eventInputs, fbCall.event)
	}

	def internalFbIndexFromName(FB fb) {
		val fbType = fb.eContainer as BaseFBType
		var index = 0;
		for (internalFb : fbType.internalFbs) {
			if (fb === internalFb) {
				return index;
			}
			index++;
		}
	}

	def protected dispatch generateStatement(ReturnStatement stmt) '''return;'''

	def protected dispatch generateStatement(IfStatement stmt) '''
		if(«stmt.expression.generateExpression») {
			«stmt.statments.generateStatementList»
		}
		«FOR elseif : stmt.elseif»
			else if(«elseif.expression.generateExpression») {
				«elseif.statements.generateStatementList»
			}
		«ENDFOR»
		«IF stmt.^else !== null»
			else {
				«stmt.^else.statements.generateStatementList»
			}
		«ENDIF»
	'''

	def protected dispatch generateStatement(CaseStatement stmt) '''
		switch («stmt.expression.generateExpression») {
			«FOR clause : stmt.^case»«clause.generateCaseClause»«ENDFOR»
			«IF stmt.^else !== null»
				default:
					«stmt.^else.statements.generateStatementList»
					break;
			«ENDIF»
		}
	'''

	def protected generateCaseClause(CaseClause clause) '''
		case «FOR value : clause.^case SEPARATOR ' case '»«value.generateExpression»:«ENDFOR»
			«clause.statements.generateStatementList»
			break;
	'''

	def protected dispatch generateStatement(ExitStatement stmt) '''break;'''

	def protected dispatch generateStatement(ContinueStatement stmt) '''continue;'''
	
	def private dispatch containedStatements(IfStatement statement) {
		val EList<Statement> list = new BasicEList<Statement>()
		list.addAll(statement.statments.statements)
		for(elseif : statement.elseif) {
			if(elseif?.statements?.statements !== null) {
				list.addAll(elseif.statements.statements)
			}
		}
		if(statement?.^else?.statements?.statements !== null) {
			list.addAll(statement.^else.statements.statements)
		}
		return list
	}
	
	def private dispatch containedStatements(CaseStatement statement) {
		val EList<Statement> list = new BasicEList<Statement>()
		for(casz : statement.^case) {
			if(casz?.statements?.statements !== null) {
				list.addAll(casz.statements.statements)
			}
		}
		if(statement.^else.statements.statements !== null) list.addAll(statement.^else.statements.statements)
		
		return list
	}
	
	def private dispatch containedStatements(ForStatement statement) {
		statement.statements.statements		
	}
	
	def private dispatch containedStatements(WhileStatement statement) {
		statement.statements.statements
	}
	
	def private dispatch containedStatements(RepeatStatement statement) {
		statement.statements.statements
	}
	
	def private dispatch containedStatements(Statement statement) {
		val EList<Statement> list = new BasicEList<Statement>()
		return list
	}
	
	def private indexForLoopVariables(ForStatement forStatement) {
		val forRootAlgorithm = forStatement.rootContainer as StructuredTextAlgorithm
		
		var Statement searchItem = forStatement
		var container = forStatement.eContainer.eContainer
		var containmentLevel = 0
		val indexList = new ArrayList<Integer>()
		while(container !== forRootAlgorithm) {
			if(container instanceof Statement) {
				val forStatementsInContainer = container.containedStatements.filter[it instanceof ForStatement]
				for(var i = 0; i < forStatementsInContainer.size; i++) {
					if(searchItem === forStatementsInContainer.get(i)) {
						indexList.add(i)
					}
				}
				containmentLevel++;
				searchItem = container
			}
			container = container.eContainer.eContainer
		}
		val forStatementsInAlgorithm = forRootAlgorithm.statements.statements.filter[it instanceof ForStatement]
		
		for(var i = 0; i < forStatementsInAlgorithm.size; i++) {
			if(searchItem === forStatementsInAlgorithm.get(i)) {
				indexList.add(i)
			}
		}
		
		var indexString = ""
		
		for(Integer i : indexList.reverse) {
			indexString += "_" + i			
		}
		return indexString
	}

	def protected dispatch generateStatement(ForStatement stmt) {
		val loopVarIndex = stmt.indexForLoopVariables 
	'''
		const auto by«loopVarIndex» = «IF stmt.by !== null »«stmt.by.generateExpression»«ELSE»1«ENDIF»;
		const auto to«loopVarIndex» = «stmt.to.generateExpression»;
		for(«stmt.variable.generateExpression» = «stmt.from.generateExpression»;
		    (by«loopVarIndex» >  0 && «stmt.variable.generateExpression» <= to«loopVarIndex») ||
		    (by«loopVarIndex» <= 0 && «stmt.variable.generateExpression» >= to«loopVarIndex»);
		    «stmt.variable.generateExpression» = «stmt.variable.generateExpression» + by«loopVarIndex»){
			«stmt.statements.generateStatementList»
		}
	'''
	}

	def protected dispatch generateStatement(WhileStatement stmt) '''
		while(«stmt.expression.generateExpression») {
		  «stmt.statements.generateStatementList»
		}
	'''

	def protected dispatch generateStatement(RepeatStatement stmt) '''
		do {
		  «stmt.statements.generateStatementList»
		} while(!((«stmt.expression.generateExpression»)));
	'''

	def protected generateBinaryOperator(BinaryOperator op) {
		switch (op) {
			case OR: '''||'''
			case XOR: '''^'''
			case AND: '''&&'''
			case AMPERSAND: '''&&'''
			case EQ: '''=='''
			case NE: '''!='''
			case LT: '''<'''
			case LE: '''<='''
			case GT: '''>'''
			case GE: '''>='''
			case MOD: '''%'''
			default:
				throw new UnsupportedOperationException('''The operator «op» is not supported''')
		}
	}

	def protected generateUnaryOperator(UnaryOperator op) {
		switch (op) {
			case MINUS: '''-'''
			case PLUS: '''+'''
			case NOT: '''!'''
		}
	}

	def protected dispatch CharSequence generateExpression(Call expr) {
		'''«expr.func»(«FOR arg : expr.args SEPARATOR ', '»«arg.generateExpression»«ENDFOR»)'''
	}

	def protected dispatch CharSequence generateExpression(InArgument arg) {
		arg.expr.generateExpression
	}

	def protected dispatch CharSequence generateExpression(BinaryExpression expr) {
		switch (expr.operator) {
			case POWER: '''EXPT(«expr.left.generateExpression», «expr.right.generateExpression»)'''
			case ADD: '''ADD(«expr.left.generateExpression», «expr.right.generateExpression»)'''
			case SUB: '''SUB(«expr.left.generateExpression», «expr.right.generateExpression»)'''
			case DIV: '''DIV(«expr.left.generateExpression», «expr.right.generateExpression»)'''
			case MUL: '''MUL(«expr.left.generateExpression», «expr.right.generateExpression»)'''
			default: '''(«expr.left.generateExpression» «expr.operator.generateBinaryOperator» «expr.right.generateExpression»)'''
		}
	}

	def protected dispatch generateExpression(TimeLiteral expr) {
		'''«new DatetimeLiteral(expr.literal)»'''
	}

	def protected dispatch CharSequence generateExpression(
		UnaryExpression expr) '''(«expr.operator.generateUnaryOperator» «expr.expression.generateExpression»)'''

	def protected dispatch CharSequence generateExpression(BoolLiteral expr) '''«expr.value.toString»'''

	def protected dispatch CharSequence generateExpression(IntLiteral expr) '''«expr.value.toString»'''

	def protected dispatch CharSequence generateExpression(RealLiteral expr) '''«expr.value.toString»'''

	def protected dispatch CharSequence generateExpression(StringLiteral expr) '''"«expr.value.convertToJavaString»"'''

	def protected dispatch CharSequence generateExpression(ArrayVariable expr) '''
		«expr.array.generateExpression»«FOR index : expr.index BEFORE '[' SEPARATOR '][' AFTER ']'»«index.generateExpression»«ENDFOR»
	'''

	def protected dispatch CharSequence generateExpression(
		AdapterVariable expr) '''«expr.curr.generateExpression».«expr.^var.name»()«if(!(expr.eContainer instanceof AdapterVariable))expr.generateBitaccess»'''

	def protected dispatch CharSequence generateExpression(AdapterRoot expr) '''«expr.adapter.generateVarAccess»'''

	def generateStructAdapterVarAccess(
		EList<VarDeclaration> list) '''«FOR variable : list BEFORE '.' SEPARATOR '.'»«variable.name»()«ENDFOR»'''

	def protected dispatch CharSequence generateExpression(
		PrimaryVariable expr) '''«expr.^var.generateVarAccess»«expr.generateBitaccess»'''

	def protected generateVarAccessLocal(LocalVariable variable) '''«EXPORT_PREFIX»«variable.name»'''

	def protected dispatch generateVarAccess(VarDeclaration variable) {
		if (variable.eContainer.eContainer instanceof AdapterFBType) {
			'''«variable.name»()'''
		} else {
			'''«EXPORT_PREFIX»«variable.name»()'''
		}
	}

	def protected dispatch generateVarAccess(LocalVariable variable) '''«IF variable.located
			»«variable.generateVarAccessLocated»«ELSE»«variable.generateVarAccessLocal»«ENDIF»'''

	def protected generateVarAccessLocated(LocalVariable variable) '''«IF variable.array
				»«variable.generateVarAccessLocal»«ELSE»«variable.location.generateExpression»«generateBitaccess(variable, variable.location.extractTypeInformation,variable.extractTypeInformation,0)»«ENDIF»'''

	def protected generateBitaccess(AdapterVariable variable) {
		if (null !== variable.part) {
			val lastvar = variable.^var
			generateBitaccess(lastvar, variable.part)
		}
	}

	def protected generateBitaccess(PrimaryVariable variable) {
		if (null !== variable.part) {
			generateBitaccess(variable.^var, variable.part)
		}
	}
	
	def protected partialAccessTypeName(PartialAccess part) {
		if (part.bitaccess)        FordiacKeywords.BOOL
		else if (part.byteaccess)  FordiacKeywords.BYTE
		else if (part.wordaccess)  FordiacKeywords.WORD
		else if (part.dwordaccess) FordiacKeywords.DWORD
		else                       ""
	}
	
	def protected generateBitaccess(VarDeclaration variable, PartialAccess part) {
		val maxVarBitIndex = variable.BitSize
		val endBitIndexAccessor = part.BitSize * (part.index + 1)
		if(maxVarBitIndex > endBitIndexAccessor) {
			'''.partial<CIEC_«part.partialAccessTypeName»,«Long.toString(part.index)»>()'''
		}
		
	}

	def protected generateBitaccess(VarDeclaration variable, CharSequence dataType, CharSequence accessorType, int index) {
		if (BitSize(accessorType) > 0 && variable.array &&
			variable.arraySize * BitSize(dataType) > BitSize(accessorType)) {
			'''.partial<CIEC_«accessorType»,«Long.toString(index)»>()'''
		} else if (BitSize(dataType) == BitSize(accessorType)) {
			''''''
		} else {
			'''''' // This should never happen - we cannot access more bits than are available in the source type
		}
	}

	def private extractTypeInformationWithPartialAccess(PartialAccess part, CharSequence DataType) {
		if (null !== part) {
			if (part.bitaccess)
				FordiacKeywords.BOOL
			else if (part.byteaccess)
				FordiacKeywords.BYTE
			else if (part.wordaccess)
				FordiacKeywords.WORD
			else if (part.dwordaccess)
				FordiacKeywords.DWORD
			else
				""
		} else
			DataType
	}

	def private extractTypeInformation(PrimaryVariable variable, CharSequence DataType) {
		if (null !== variable.part) {
			extractTypeInformationWithPartialAccess(variable.part, DataType)
		} else {
			DataType
		}
	}

	def protected dispatch CharSequence extractTypeInformation(PrimaryVariable variable) {
		variable.extractTypeInformation(variable.^var.extractTypeInformation)
	}

	def protected dispatch extractTypeInformation(VarDeclaration variable) {
		variable.type.name
	}
	
	def protected dispatch extractTypeInformation(AdapterVariable variable) {
		variable.^var.type.name
	}

}
