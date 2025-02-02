/**
 * generated by Xtext 2.25.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.structuredText.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fordiac.ide.model.structuredtext.structuredText.ElseClause;
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.ElseIfClause;
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.Expression;
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.IfStatement;
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.StatementList;
import org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>If Statement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.impl.IfStatementImpl#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.impl.IfStatementImpl#getStatments <em>Statments</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.impl.IfStatementImpl#getElseif <em>Elseif</em>}</li>
 *   <li>{@link org.eclipse.fordiac.ide.model.structuredtext.structuredText.impl.IfStatementImpl#getElse <em>Else</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IfStatementImpl extends StatementImpl implements IfStatement
{
  /**
   * The cached value of the '{@link #getExpression() <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpression()
   * @generated
   * @ordered
   */
  protected Expression expression;

  /**
   * The cached value of the '{@link #getStatments() <em>Statments</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getStatments()
   * @generated
   * @ordered
   */
  protected StatementList statments;

  /**
   * The cached value of the '{@link #getElseif() <em>Elseif</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElseif()
   * @generated
   * @ordered
   */
  protected EList<ElseIfClause> elseif;

  /**
   * The cached value of the '{@link #getElse() <em>Else</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElse()
   * @generated
   * @ordered
   */
  protected ElseClause else_;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected IfStatementImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return StructuredTextPackage.Literals.IF_STATEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Expression getExpression()
  {
    return expression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExpression(Expression newExpression, NotificationChain msgs)
  {
    Expression oldExpression = expression;
    expression = newExpression;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StructuredTextPackage.IF_STATEMENT__EXPRESSION, oldExpression, newExpression);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setExpression(Expression newExpression)
  {
    if (newExpression != expression)
    {
      NotificationChain msgs = null;
      if (expression != null)
        msgs = ((InternalEObject)expression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StructuredTextPackage.IF_STATEMENT__EXPRESSION, null, msgs);
      if (newExpression != null)
        msgs = ((InternalEObject)newExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StructuredTextPackage.IF_STATEMENT__EXPRESSION, null, msgs);
      msgs = basicSetExpression(newExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StructuredTextPackage.IF_STATEMENT__EXPRESSION, newExpression, newExpression));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public StatementList getStatments()
  {
    return statments;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetStatments(StatementList newStatments, NotificationChain msgs)
  {
    StatementList oldStatments = statments;
    statments = newStatments;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StructuredTextPackage.IF_STATEMENT__STATMENTS, oldStatments, newStatments);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setStatments(StatementList newStatments)
  {
    if (newStatments != statments)
    {
      NotificationChain msgs = null;
      if (statments != null)
        msgs = ((InternalEObject)statments).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StructuredTextPackage.IF_STATEMENT__STATMENTS, null, msgs);
      if (newStatments != null)
        msgs = ((InternalEObject)newStatments).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StructuredTextPackage.IF_STATEMENT__STATMENTS, null, msgs);
      msgs = basicSetStatments(newStatments, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StructuredTextPackage.IF_STATEMENT__STATMENTS, newStatments, newStatments));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<ElseIfClause> getElseif()
  {
    if (elseif == null)
    {
      elseif = new EObjectContainmentEList<ElseIfClause>(ElseIfClause.class, this, StructuredTextPackage.IF_STATEMENT__ELSEIF);
    }
    return elseif;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ElseClause getElse()
  {
    return else_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetElse(ElseClause newElse, NotificationChain msgs)
  {
    ElseClause oldElse = else_;
    else_ = newElse;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StructuredTextPackage.IF_STATEMENT__ELSE, oldElse, newElse);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setElse(ElseClause newElse)
  {
    if (newElse != else_)
    {
      NotificationChain msgs = null;
      if (else_ != null)
        msgs = ((InternalEObject)else_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StructuredTextPackage.IF_STATEMENT__ELSE, null, msgs);
      if (newElse != null)
        msgs = ((InternalEObject)newElse).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StructuredTextPackage.IF_STATEMENT__ELSE, null, msgs);
      msgs = basicSetElse(newElse, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, StructuredTextPackage.IF_STATEMENT__ELSE, newElse, newElse));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case StructuredTextPackage.IF_STATEMENT__EXPRESSION:
        return basicSetExpression(null, msgs);
      case StructuredTextPackage.IF_STATEMENT__STATMENTS:
        return basicSetStatments(null, msgs);
      case StructuredTextPackage.IF_STATEMENT__ELSEIF:
        return ((InternalEList<?>)getElseif()).basicRemove(otherEnd, msgs);
      case StructuredTextPackage.IF_STATEMENT__ELSE:
        return basicSetElse(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case StructuredTextPackage.IF_STATEMENT__EXPRESSION:
        return getExpression();
      case StructuredTextPackage.IF_STATEMENT__STATMENTS:
        return getStatments();
      case StructuredTextPackage.IF_STATEMENT__ELSEIF:
        return getElseif();
      case StructuredTextPackage.IF_STATEMENT__ELSE:
        return getElse();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case StructuredTextPackage.IF_STATEMENT__EXPRESSION:
        setExpression((Expression)newValue);
        return;
      case StructuredTextPackage.IF_STATEMENT__STATMENTS:
        setStatments((StatementList)newValue);
        return;
      case StructuredTextPackage.IF_STATEMENT__ELSEIF:
        getElseif().clear();
        getElseif().addAll((Collection<? extends ElseIfClause>)newValue);
        return;
      case StructuredTextPackage.IF_STATEMENT__ELSE:
        setElse((ElseClause)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case StructuredTextPackage.IF_STATEMENT__EXPRESSION:
        setExpression((Expression)null);
        return;
      case StructuredTextPackage.IF_STATEMENT__STATMENTS:
        setStatments((StatementList)null);
        return;
      case StructuredTextPackage.IF_STATEMENT__ELSEIF:
        getElseif().clear();
        return;
      case StructuredTextPackage.IF_STATEMENT__ELSE:
        setElse((ElseClause)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case StructuredTextPackage.IF_STATEMENT__EXPRESSION:
        return expression != null;
      case StructuredTextPackage.IF_STATEMENT__STATMENTS:
        return statments != null;
      case StructuredTextPackage.IF_STATEMENT__ELSEIF:
        return elseif != null && !elseif.isEmpty();
      case StructuredTextPackage.IF_STATEMENT__ELSE:
        return else_ != null;
    }
    return super.eIsSet(featureID);
  }

} //IfStatementImpl
