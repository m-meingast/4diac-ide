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
package org.eclipse.fordiac.ide.model.value;

import org.eclipse.fordiac.ide.model.Messages;

public final class WStringValueConverter extends AbstractStringValueConverter {
	public static final WStringValueConverter INSTANCE = new WStringValueConverter();

	private WStringValueConverter() {
	}

	@Override
	public String toValue(final String string) throws IllegalArgumentException {
		if (string.length() < 1 || string.charAt(0) != '"') {
			throw new IllegalArgumentException(Messages.VALIDATOR_IllegalStringLiteral);
		}
		return super.toValue(string);
	}

	@Override
	public String toString(final String value) {
		return toString(value, true);
	}
}