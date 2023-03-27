/*******************************************************************************
 * Copyright (c) 2022 - 2023 Martin Erich Jobst
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
package org.eclipse.fordiac.ide.model.eval.variable

import java.util.List
import java.util.regex.Pattern
import org.eclipse.fordiac.ide.model.data.ArrayType
import org.eclipse.fordiac.ide.model.data.DataFactory
import org.eclipse.fordiac.ide.model.data.DataType
import org.eclipse.fordiac.ide.model.data.Subrange
import org.eclipse.fordiac.ide.model.eval.value.ArrayValue
import org.eclipse.fordiac.ide.model.eval.value.Value
import org.eclipse.xtend.lib.annotations.Accessors

import static org.eclipse.fordiac.ide.model.eval.variable.VariableOperations.*

import static extension java.lang.Math.*
import static extension org.eclipse.emf.ecore.util.EcoreUtil.*
import static extension org.eclipse.fordiac.ide.model.eval.value.ValueOperations.*

class ArrayVariable extends AbstractVariable<ArrayValue> implements Iterable<Variable<?>> {
	static final Pattern ARRAY_PATTERN = Pattern.compile(",(?=(?:[^\"']*(?:(?:\"[^\"]*\")|(?:\'[^\']*\')))*[^\"']*$)")

	@Accessors final DataType elementType
	@Accessors final List<Variable<?>> elements
	@Accessors final ArrayValue value

	new(String name, ArrayType type) {
		super(name, type)
		if (!type.subranges.forall[isSetLowerLimit && isSetUpperLimit]) {
			throw new IllegalArgumentException("Cannot instantiate array variable with unknown bounds")
		}
		elementType = if (type.subranges.size > 1)
			newArrayType(type.baseType, type.subranges.tail.map[copy])
		else
			type.baseType
		elements = (type.subranges.head.lowerLimit .. type.subranges.head.upperLimit).map [
			newVariable(it.toString, elementType)
		].toList.immutableCopy
		value = new ArrayValue(type, elements)
	}

	new(String name, ArrayType type, String value) {
		this(name, type)
		if(!value.nullOrEmpty) this.value = value
	}

	new(String name, ArrayType type, Value value) {
		this(name, type.withKnownBounds(value))
		if(value !== null) this.value = value
	}

	override setValue(Value value) {
		if (value instanceof ArrayValue) {
			(this.value.start.max(value.start) .. this.value.end.min(value.end)).forEach [ index |
				this.value.get(index).value = value.get(index).value.castValue(elementType)
			]
		} else
			throw new ClassCastException('''Cannot assign value with incompatible type «value.type.name» as «type.name»''')
	}

	override setValue(String value) {
		val trimmed = value.trim
		if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
			throw new IllegalArgumentException("Not a valid array value")
		}
		val inner = trimmed.substring(1, trimmed.length - 1)
		ARRAY_PATTERN.split(inner).forEach [ elem, index |
			elements.get(index).value = elem.trim
		]
	}

	override validateValue(String value) {
		val trimmed = value.trim
		if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
			return false
		}
		val inner = trimmed.substring(1, trimmed.length - 1)
		val elementStrings = ARRAY_PATTERN.split(inner)
		if(elementStrings.size > elements.size) {
			return false
		}
		for (i : 0 ..< elementStrings.size) {
			if (!elements.get(i).validateValue(elementStrings.get(i).trim)) {
				return false
			}
		}
		return true
	}

	override ArrayType getType() {
		super.type as ArrayType
	}

	def protected static ArrayType withKnownBounds(ArrayType type, Value value) {
		if (value instanceof ArrayValue) {
			type.withKnownBounds(value.type.subranges)
		} else
			throw new ClassCastException('''Cannot assign value with incompatible type «value.type.name» as «type.name»''')
	}

	def protected static ArrayType withKnownBounds(ArrayType type, List<Subrange> knownSubranges) {
		if (!type.subranges.forall[isSetLowerLimit && isSetUpperLimit]) {
			type.baseType.newArrayType(type.subranges.indexed.map [
				it.value.isSetLowerLimit && it.value.isSetUpperLimit ? it.value : knownSubranges.get(it.key)
			].map[copy])
		} else
			type
	}

	def static ArrayType newArrayType(DataType arrayBaseType, Subrange... arraySubranges) {
		arrayBaseType.newArrayType(arraySubranges as Iterable<Subrange>)
	}

	def static ArrayType newArrayType(DataType arrayBaseType, Iterable<Subrange> arraySubranges) {
		DataFactory.eINSTANCE.createArrayType => [
			name = '''ARRAY [«arraySubranges.map['''«IF isSetLowerLimit && isSetUpperLimit»«lowerLimit»..«upperLimit»«ELSE»*«ENDIF»'''].join(", ")»] OF «arrayBaseType.name»'''
			baseType = arrayBaseType
			subranges.addAll(arraySubranges)
		]
	}

	def static newSubrange(int lower, int upper) {
		DataFactory.eINSTANCE.createSubrange => [
			lowerLimit = lower
			upperLimit = upper
		]
	}

	override iterator() {
		elements.iterator
	}
}
