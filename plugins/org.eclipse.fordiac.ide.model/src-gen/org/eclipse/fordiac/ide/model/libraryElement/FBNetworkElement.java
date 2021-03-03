/********************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger
 *    - initial API and implementation and/or initial documentation
 ********************************************************************************/
package org.eclipse.fordiac.ide.model.libraryElement;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>FB
 * Network Element</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.eclipse.fordiac.ide.model.libraryElement.FBNetworkElement#getInterface
 * <em>Interface</em>}</li>
 * <li>{@link org.eclipse.fordiac.ide.model.libraryElement.FBNetworkElement#getMapping
 * <em>Mapping</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getFBNetworkElement()
 * @model
 * @generated
 */
public interface FBNetworkElement extends TypedConfigureableObject, PositionableElement {
	/**
	 * Returns the value of the '<em><b>Interface</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interface</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Interface</em>' containment reference.
	 * @see #setInterface(InterfaceList)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getFBNetworkElement_Interface()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	InterfaceList getInterface();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.fordiac.ide.model.libraryElement.FBNetworkElement#getInterface
	 * <em>Interface</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Interface</em>' containment reference.
	 * @see #getInterface()
	 * @generated
	 */
	void setInterface(InterfaceList value);

	/**
	 * Returns the value of the '<em><b>Mapping</b></em>' reference. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mapping</em>' reference isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Mapping</em>' reference.
	 * @see #setMapping(Mapping)
	 * @see org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage#getFBNetworkElement_Mapping()
	 * @model
	 * @generated
	 */
	Mapping getMapping();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.fordiac.ide.model.libraryElement.FBNetworkElement#getMapping
	 * <em>Mapping</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Mapping</em>' reference.
	 * @see #getMapping()
	 * @generated
	 */
	void setMapping(Mapping value);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true"
	 * @generated
	 */
	Resource getResource();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model required="true" nameRequired="true"
	 * @generated
	 */
	IInterfaceElement getInterfaceElement(String name);

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true"
	 * @generated
	 */
	FBNetworkElement getOpposite();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true"
	 * @generated
	 */
	FBNetwork getFbNetwork();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model
	 * @generated
	 */
	void checkConnections();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true"
	 * @generated
	 */
	boolean isMapped();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true"
	 * @generated
	 */
	@Override
	FBType getType();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true"
	 * @generated
	 */
	boolean isNestedInSubApp();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" required="true"
	 * @generated
	 */
	FBNetworkElement getOuterFBNetworkElement();

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @model kind="operation" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 * @generated
	 */
	boolean isContainedInTypedInstance();

} // FBNetworkElement
