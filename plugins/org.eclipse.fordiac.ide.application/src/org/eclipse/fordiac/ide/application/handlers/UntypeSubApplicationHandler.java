/*******************************************************************************
 * Copyright (c) 2019, 2021 Johannes Kepler University Linz
 *                          Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl - initial API and implementation and/or initial documentation
 *   Bianca Wiesmayr - reworked for breadcrumb editor
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.fordiac.ide.application.editparts.SubAppForFBNetworkEditPart;
import org.eclipse.fordiac.ide.application.editparts.UISubAppNetworkEditPart;
import org.eclipse.fordiac.ide.model.commands.change.UntypeSubAppCommand;
import org.eclipse.fordiac.ide.model.libraryElement.SubApp;
import org.eclipse.fordiac.ide.model.ui.editors.HandlerHelper;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CommandStackEvent;
import org.eclipse.gef.commands.CommandStackEventListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class UntypeSubApplicationHandler extends AbstractHandler implements CommandStackEventListener {
	private CommandStack commandStack;

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IEditorPart editor = HandlerUtil.getActiveEditor(event);
		final ISelection selection = HandlerUtil.getCurrentSelection(event);
		final SubApp subApp = getSelectedSubApp(selection);
		if (null != subApp) {
			if (commandStack != null) {
				commandStack.removeCommandStackEventListener(this);
			}

			commandStack = HandlerHelper.getCommandStack(editor);
			commandStack.execute(new UntypeSubAppCommand(subApp));

			refreshSelection(subApp);

			commandStack.addCommandStackEventListener(this);
		}
		return Status.OK_STATUS;
	}

	@Override
	public void setEnabled(final Object evaluationContext) {
		final Object selection = HandlerUtil.getVariable(evaluationContext, ISources.ACTIVE_CURRENT_SELECTION_NAME);
		final SubApp subApp = getSelectedSubApp(selection);
		setBaseEnabled((null != subApp) && (subApp.isTyped()));
	}

	private static SubApp getSelectedSubApp(final Object selection) {
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection structSel = ((IStructuredSelection) selection);
			if (!structSel.isEmpty() && (structSel.size() == 1)) {
				return getSubApp(structSel.getFirstElement());
			}
		}
		return null;
	}

	private static SubApp getSubApp(final Object currentElement) {
		if (currentElement instanceof SubApp) {
			return (SubApp) currentElement;
		} else if (currentElement instanceof SubAppForFBNetworkEditPart) {
			return ((SubAppForFBNetworkEditPart) currentElement).getModel();
		} else if (currentElement instanceof UISubAppNetworkEditPart) {
			return (SubApp) ((UISubAppNetworkEditPart) currentElement).getModel().eContainer();
		}
		return null;
	}

	@Override
	public void stackChanged(CommandStackEvent event) {
		if (event.getCommand() instanceof UntypeSubAppCommand
				&& (event.getDetail() == CommandStack.POST_UNDO || event.getDetail() == CommandStack.POST_REDO)) {
			refreshSelection(((UntypeSubAppCommand) event.getCommand()).getSubapp());
		}
	}

	private void refreshSelection(final SubApp subapp) {
		final IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		HandlerHelper.getViewer(editor).deselectAll();
		HandlerHelper.selectElement(subapp, HandlerHelper.getViewer(editor));
	}

	@Override
	public void dispose() {
		if (commandStack != null) {
			commandStack.removeCommandStackEventListener(this);
		}
		super.dispose();
	}
}
