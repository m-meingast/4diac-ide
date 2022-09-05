/*******************************************************************************
 * Copyright (c) 2022 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.policies;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.fordiac.ide.application.editparts.IContainerEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;

class ContainerResizeTracker extends ResizeTracker {
	private Rectangle minBounds;

	public ContainerResizeTracker(final IContainerEditPart owner, final int direction) {
		super(owner, direction);
	}

	@Override
	protected IContainerEditPart getOwner() {
		return (IContainerEditPart) super.getOwner();
	}

	@Override
	protected Dimension getMinimumSizeFor(final ChangeBoundsRequest request) {
		final Rectangle bounds = getOwner().getFigure().getBounds();
		// also check the top left corner as content is not moved during resize
		final int minWidth = getMinWidth(bounds, getResizeDirection());
		final int minHeight = getMinHeight(bounds, getResizeDirection());
		return new Dimension(minWidth, minHeight);
	}

	@Override
	protected boolean handleDragStarted() {
		updateMinBounds();
		return super.handleDragStarted();
	}

	@Override
	protected void enforceConstraintsForResize(final ChangeBoundsRequest changeBoundsRequest) {
		super.enforceConstraintsForResize(changeBoundsRequest);
		if (getOwner() != null && isTopOrLeft()) {
			// ensure that on minimum size the bottom right corner is not moving, this is has potential to be upstreamed
			// to GEF
			final PrecisionRectangle originalConstraint = new PrecisionRectangle(getOwner().getFigure().getBounds());
			getOwner().getFigure().translateToAbsolute(originalConstraint);
			final PrecisionRectangle manipulatedConstraint = new PrecisionRectangle(
					changeBoundsRequest.getTransformedRectangle(originalConstraint));
			final Point origBR = originalConstraint.getBottomRight();
			final Point newBR = manipulatedConstraint.getBottomRight();

			if (isLeftResize() && (origBR.x < newBR.x)) {
				final Point moveDelta = changeBoundsRequest.getMoveDelta();
				moveDelta.x -= (newBR.x - origBR.x);
				changeBoundsRequest.setMoveDelta(moveDelta);
			}

			if (isTopResize() && (origBR.y < newBR.y)) {
				final Point moveDelta = changeBoundsRequest.getMoveDelta();
				moveDelta.y -= (newBR.y - origBR.y);
				changeBoundsRequest.setMoveDelta(moveDelta);
			}
		}
	}

	private boolean isTopOrLeft() {
		return isLeftResize() || isTopResize();
	}

	private boolean isLeftResize() {
		return getResizeDirection() == PositionConstants.WEST || getResizeDirection() == PositionConstants.NORTH_WEST
				|| getResizeDirection() == PositionConstants.SOUTH_WEST;
	}

	private boolean isTopResize() {
		return getResizeDirection() == PositionConstants.NORTH || getResizeDirection() == PositionConstants.NORTH_WEST
				|| getResizeDirection() == PositionConstants.NORTH_EAST;
	}

	private void updateMinBounds() {
		final Rectangle containerBounds = getOwner().getFigure().getBounds();
		final GraphicalEditPart contentEP = getOwner().getContentEP();
		if (contentEP != null) { // this should never be the case but just for safety
			final Rectangle contentFigureBounds = contentEP.getFigure().getBounds();
			minBounds = getOwner().getMinContentBounds();
			// consider container size requirements
			minBounds.x -= (contentFigureBounds.x - containerBounds.x);
			minBounds.y -= (contentFigureBounds.y - containerBounds.y);
			minBounds.width += containerBounds.width - contentFigureBounds.width;
			minBounds.height += containerBounds.height - contentFigureBounds.height;
		} else {
			minBounds = getOwner().getDefaultContentBounds();
		}
	}

	private int getMinHeight(final Rectangle bounds, final int resizeDirection) {
		int height = minBounds.height;
		switch (resizeDirection) {
		case PositionConstants.NORTH:
		case PositionConstants.NORTH_WEST:
		case PositionConstants.NORTH_EAST:
			// ensure that the bottom border is not lost
			height += bounds.height - (minBounds.y - bounds.y) - minBounds.height;
			break;
		case PositionConstants.SOUTH:
		case PositionConstants.SOUTH_EAST:
		case PositionConstants.SOUTH_WEST:
			// ensure that the top border is not lost
			height += (minBounds.y - bounds.y);
			break;
		default:
			// we don't care about WEST and EAST as they are not define the height
			break;
		}

		return height;
	}

	private int getMinWidth(final Rectangle bounds, final int resizeDirection) {
		int width = minBounds.width;
		switch (resizeDirection) {
		case PositionConstants.WEST:
		case PositionConstants.NORTH_WEST:
		case PositionConstants.SOUTH_WEST:
			// ensure that the right border is not lost
			width += bounds.width - (minBounds.x - bounds.x) - minBounds.width;
			break;
		case PositionConstants.EAST:
		case PositionConstants.NORTH_EAST:
		case PositionConstants.SOUTH_EAST:
			// ensure that the top border is not lost
			width += (minBounds.x - bounds.x);
			break;
		default:
			// we don't care about NORTH and SOUTH as they are not define the height
			break;
		}
		return width;
	}
}