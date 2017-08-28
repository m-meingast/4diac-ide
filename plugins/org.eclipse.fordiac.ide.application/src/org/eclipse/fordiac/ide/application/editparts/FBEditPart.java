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

import org.eclipse.draw2d.IFigure;
import org.eclipse.fordiac.ide.application.Messages;
import org.eclipse.fordiac.ide.application.actions.OpenCompositeInstanceViewerAction;
import org.eclipse.fordiac.ide.application.figures.FBFigure;
import org.eclipse.fordiac.ide.model.libraryElement.CompositeFBType;
import org.eclipse.fordiac.ide.model.libraryElement.FB;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.ZoomManager;

/**
 * This class implements an EditPart for a FunctionBlock.
 */
public class FBEditPart extends AbstractFBNElementEditPart {

	public FBEditPart(final ZoomManager zoomManager) {
		super(zoomManager);
	}

	/**
	 * Creates the figure (for the specified model) to be used as this parts
	 * visuals.
	 * 
	 * @return IFigure The figure for the model
	 */
	@Override
	protected IFigure createFigureForModel() {
		// extend this if FunctionBlock gets extended!
		FBFigure f = null;
		if (getModel() != null) {
			f = new FBFigure(getModel(), this);
		} else {
			throw new IllegalArgumentException(Messages.FBEditPart_ERROR_UnsupportedFBType);
		}
		return f;
	}

	
	@Override
	public FB getModel() {
		return (FB) super.getModel();
	}

	@Override
	public void performRequest(Request request) {
		if (request.getType().equals(RequestConstants.REQ_OPEN)
				&& getModel() != null
				&& getModel().getType() instanceof CompositeFBType) {
			new OpenCompositeInstanceViewerAction(this, getModel()).run();
		} else {
			super.performRequest(request);
		}
	}

}
