/**
 * *******************************************************************************
 * Copyright (c) 2008 - 2018 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *    Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger, Martin Jobst
 *      - initial API and implementation and/or initial documentation
 * *******************************************************************************
 */
package org.eclipse.fordiac.ide.model.libraryElement;

import org.eclipse.emf.common.util.EList;

/** <!-- begin-user-doc --> A representation of the model object '<em><b>Var Declaration</b></em>'. <!-- end-user-doc
 * -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration#getArraySize <em>Array Size</em>}</li>
 * <li>{@link org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration#getWiths <em>Withs</em>}</li>
 * <li>{@link org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getVarDeclaration()
 * @model
 * @generated */
public interface VarDeclaration extends IInterfaceElement {
	/** Returns the value of the '<em><b>Array Size</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Array Size</em>' attribute.
	 * @see #setArraySize(int)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getVarDeclaration_ArraySize()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Int" extendedMetaData="kind='attribute' name='arraySize'"
	 * @generated */
	int getArraySize();

	/** Sets the value of the '{@link org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration#getArraySize <em>Array
	 * Size</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Array Size</em>' attribute.
	 * @see #getArraySize()
	 * @generated */
	void setArraySize(int value);

	/** Returns the value of the '<em><b>Withs</b></em>' reference list. The list contents are of type
	 * {@link org.eclipse.fordiac.ide.model.libraryElement.With}. It is bidirectional and its opposite is
	 * '{@link org.eclipse.fordiac.ide.model.libraryElement.With#getVariables <em>Variables</em>}'. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Withs</em>' reference list.
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getVarDeclaration_Withs()
	 * @see org.eclipse.fordiac.ide.model.libraryElement.With#getVariables
	 * @model opposite="variables"
	 * @generated */
	EList<With> getWiths();

	/** Returns the value of the '<em><b>Value</b></em>' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(Value)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getVarDeclaration_Value()
	 * @model containment="true" resolveProxies="true"
	 * @generated */
	Value getValue();

	/** Sets the value of the '{@link org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration#getValue
	 * <em>Value</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated */
	void setValue(Value value);

	/** <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated */
	boolean isArray();

} // VarDeclaration
