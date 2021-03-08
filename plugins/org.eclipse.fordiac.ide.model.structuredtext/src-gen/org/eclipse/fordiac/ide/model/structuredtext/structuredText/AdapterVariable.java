/**
 * generated by Xtext 2.24.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.structuredText;

import org.eclipse.fordiac.ide.model.libraryElement.VarDeclaration;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Adapter Variable</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.AdapterVariable#getCurr <em>Curr</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.AdapterVariable#getVar <em>Var</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getAdapterVariable()
 * @model
 * @generated
 */
public interface AdapterVariable extends Variable
{
  /**
   * Returns the value of the '<em><b>Curr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Curr</em>' containment reference.
   * @see #setCurr(AdapterVariable)
   * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getAdapterVariable_Curr()
   * @model containment="true"
   * @generated
   */
  AdapterVariable getCurr();

  /**
   * Sets the value of the '{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.AdapterVariable#getCurr <em>Curr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Curr</em>' containment reference.
   * @see #getCurr()
   * @generated
   */
  void setCurr(AdapterVariable value);

  /**
   * Returns the value of the '<em><b>Var</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Var</em>' reference.
   * @see #setVar(VarDeclaration)
   * @see org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage#getAdapterVariable_Var()
   * @model
   * @generated
   */
  VarDeclaration getVar();

  /**
   * Sets the value of the '{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.AdapterVariable#getVar <em>Var</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Var</em>' reference.
   * @see #getVar()
   * @generated
   */
  void setVar(VarDeclaration value);

} // AdapterVariable
