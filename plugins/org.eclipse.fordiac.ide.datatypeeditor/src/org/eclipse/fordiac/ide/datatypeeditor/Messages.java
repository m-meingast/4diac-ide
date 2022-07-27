/*******************************************************************************
 * Copyright (c) 2020 Johannes Kepler University, Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Daniel Lindhuber
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.datatypeeditor;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings("squid:S3008")  // tell sonar the java naming convention does not make sense for this class
public final class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.fordiac.ide.datatypeeditor.messages"; //$NON-NLS-1$
	public static String MessageDialogTitle_SaveError;
	public static String MessageDialogContent_SaveError;
	public static String ErrorCompositeMessage;
	public static String MessageDialogTitle_OutsideWorkspaceError;
	public static String MessageDialogContent_OutsideWorkspaceError;
	public static String StructAlteringButton_Cancel;
	public static String StructAlteringButton_Save;
	public static String StructAlteringButton_SaveAs;
	public static String StructAlteringDescription;
	public static String StructViewingComposite_Headline;
	public static String WarningDialog_StructNotSaved;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
		// empty private constructor
	}
}
