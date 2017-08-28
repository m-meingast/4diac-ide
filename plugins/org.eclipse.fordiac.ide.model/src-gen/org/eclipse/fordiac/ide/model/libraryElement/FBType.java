/********************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger
 *    - initial API and implementation and/or initial documentation
 ********************************************************************************/
package org.eclipse.fordiac.ide.model.libraryElement;



/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FB Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.model.libraryElement.FBType#getInterfaceList <em>Interface List</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.libraryElement.FBType#getService <em>Service</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getFBType()
 * @model
 * @generated
 */
public interface FBType extends CompilableType {
	/**
	 * Returns the value of the '<em><b>Interface List</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interface List</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interface List</em>' containment reference.
	 * @see #setInterfaceList(InterfaceList)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getFBType_InterfaceList()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='InterfaceList' namespace='##targetNamespace'"
	 * @generated
	 */
	InterfaceList getInterfaceList();

	/**
	 * Sets the value of the '{@link org.eclipse.fordiac.ide.model.libraryElement.FBType#getInterfaceList <em>Interface List</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interface List</em>' containment reference.
	 * @see #getInterfaceList()
	 * @generated
	 */
	void setInterfaceList(InterfaceList value);
	
	/**
	 * Returns the value of the '<em><b>Service</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service</em>' containment reference.
	 * @see #setService(Service)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getFBType_Service()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='ServiceSequence' namespace='##targetNamespace'"
	 * @generated
	 */
	Service getService();

	/**
	 * Sets the value of the '{@link org.eclipse.fordiac.ide.model.libraryElement.FBType#getService <em>Service</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service</em>' containment reference.
	 * @see #getService()
	 * @generated
	 */
	void setService(Service value);

} // FBType
