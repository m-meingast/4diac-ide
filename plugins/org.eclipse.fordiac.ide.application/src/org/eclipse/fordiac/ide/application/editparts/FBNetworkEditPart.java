/*******************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl 
 *   - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.editparts;

import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.fordiac.ide.application.SpecificLayerEditPart;
import org.eclipse.fordiac.ide.application.policies.FBNetworkXYLayoutEditPolicy;
import org.eclipse.fordiac.ide.gef.editparts.AbstractFBNetworkEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.RootComponentEditPolicy;

/**
 * Edit Part for the visualization of FBNetworks.
 */
public class FBNetworkEditPart extends AbstractFBNetworkEditPart {

	/** The adapter. */
	private EContentAdapter adapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		if (!isActive()) {
			super.activate();
			((Notifier) getModel()).eAdapters().add(getContentAdapter());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		if (isActive()) {
			super.deactivate();
			((Notifier) getModel()).eAdapters().remove(getContentAdapter());

		}
	}

	/**
	 * Gets the content adapter.
	 * 
	 * @return the content adapter
	 */
	protected EContentAdapter getContentAdapter() {
		if (adapter == null) {
			adapter = new EContentAdapter() {
				@Override
				public void notifyChanged(final Notification notification) {
					int type = notification.getEventType();
					switch (type) {
					case Notification.ADD:
					case Notification.ADD_MANY:
					case Notification.REMOVE:
					case Notification.REMOVE_MANY:
						refreshChildren();
						break;
					case Notification.SET:
						break;
					}
				}
			};
		}
		return adapter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#refresh()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void refresh() {
		super.refresh();
		for (Iterator iterator = getChildren().iterator(); iterator.hasNext();) {
			EditPart ep = (EditPart) iterator.next();
			if (ep instanceof SubAppForFBNetworkEditPart) {
				ep.refresh();
			}
		}
	}

	/**
	 * Creates the EditPolicies used for this EditPart.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new RootComponentEditPolicy());
		// handles constraint changes of model elements and creation of new
		// model elements
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FBNetworkXYLayoutEditPolicy());
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (childEditPart instanceof SpecificLayerEditPart) {
			String layer = ((SpecificLayerEditPart) childEditPart).getSpecificLayer();
			IFigure layerFig = getLayer(layer);
			if (layerFig != null) {
				IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
				layerFig.add(child);
				return;
			} 
		} 
		//as some of the children are in a different layer we can not use the index given. 
		//Currently -1 seams to be the best option
		super.addChildVisual(childEditPart, -1);
	}

	@Override
	protected void removeChildVisual(EditPart childEditPart) {
		if (childEditPart instanceof SpecificLayerEditPart) {
			String layer = ((SpecificLayerEditPart) childEditPart).getSpecificLayer();
			IFigure layerFig = getLayer(layer);
			if (layerFig != null) {
				IFigure child = ((GraphicalEditPart) childEditPart).getFigure();
				layerFig.remove(child);
				return;
			} 
		}
		super.removeChildVisual(childEditPart);
	}
	
}
