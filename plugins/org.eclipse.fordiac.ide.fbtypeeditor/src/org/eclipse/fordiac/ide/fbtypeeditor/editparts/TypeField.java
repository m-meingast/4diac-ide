/*******************************************************************************
 * Copyright (c) 2011 - 2017 Profactor GmbH, fortiss GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.editparts;

import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;

public class TypeField {
	private final IInterfaceElement referencedElement;

	public IInterfaceElement getReferencedElement() {
		return referencedElement;
	}

	public TypeField(final IInterfaceElement referencedElement) {
		this.referencedElement = referencedElement;
	}

	public String getLabel() {
		return getReferencedElement().getTypeName();
	}

	public String getArrayLabel() {
		String typeLabel = getLabel();
		if (referencedElement instanceof VarDeclaration) {
			// if is array append array size
			final VarDeclaration varDec = (VarDeclaration) referencedElement;
			if (varDec.isArray()) {
				typeLabel = typeLabel + "[" + varDec.getArraySize() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return typeLabel;
	}
}
