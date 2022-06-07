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
package org.eclipse.fordiac.ide.model.eval.value

import java.math.BigInteger
import org.eclipse.fordiac.ide.model.data.BoolType
import org.eclipse.fordiac.ide.model.datatype.helper.IecTypes
import org.eclipse.fordiac.ide.model.value.BoolValueConverter

class BoolValue implements AnyBitValue {
	final boolean value;

	public static final BoolValue FALSE = new BoolValue(false)
	public static final BoolValue TRUE = new BoolValue(true)
	public static final BoolValue DEFAULT = FALSE

	private new(boolean value) {
		this.value = value;
	}

	def static toBoolValue(boolean value) { new BoolValue(value) }

	def static toBoolValue(Boolean value) { new BoolValue(value.booleanValue) }

	def static toBoolValue(String value) { BoolValueConverter.INSTANCE.toValue(value).toBoolValue }

	def static toBoolValue(AnyBitValue value) { value.boolValue.toBoolValue }

	override BoolType getType() { IecTypes.ElementaryTypes.BOOL }

	override boolValue() { value }

	override byteValue() { intValue as byte }

	override shortValue() { intValue as short }

	override intValue() { if(value) 1 else 0 }

	override longValue() { intValue }

	override BigInteger bigIntegerValue() {	BigInteger.valueOf(longValue) }
	
	override equals(Object obj) { if(obj instanceof BoolValue) value == obj.value else false }

	override hashCode() { Boolean.hashCode(value) }

	override toString() { BoolValueConverter.INSTANCE.toString(value) }
}
