/*******************************************************************************
 * Copyright (c) 2021 Johannes Kepler University Linz
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Antonio Garmend�a, Bianca Wiesmayr
 *       - initial implementation and/or documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.test.fb.interpreter.basicfb;

import org.eclipse.fordiac.ide.model.libraryElement.BasicFBType;
import org.eclipse.fordiac.ide.model.libraryElement.ServiceSequence;
import org.eclipse.fordiac.ide.test.fb.interpreter.infra.AbstractInterpreterTest;
import org.eclipse.fordiac.ide.test.fb.interpreter.infra.FBTransaction;
import org.junit.Test;

public class EventDFFTest extends AbstractInterpreterTest {

	public EventDFFTest() {
		// do nothing
	}

	@Test
	public void test() throws Exception {
		final BasicFBType fb = loadFBType("E_D_FF"); //$NON-NLS-1$
		ServiceSequence seq = fb.getService().getServiceSequence().get(0);

		setVariable(fb, "D", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransaction("CLK")); //$NON-NLS-1$

		runTest(fb, seq);

		seq = newServiceSequence(fb);
		setVariable(fb, "D", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransaction("CLK", "EO", "Q:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		runTest(fb, seq);

		seq = newServiceSequence(fb);
		setVariable(fb, "D", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransaction("CLK", "EO", "Q:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		runTest(fb, seq, "SET"); //$NON-NLS-1$

		seq = newServiceSequence(fb);
		setVariable(fb, "D", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransaction("CLK", "EO", "Q:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		runTest(fb, seq, "RESET"); //$NON-NLS-1$

		seq = newServiceSequence(fb);
		setVariable(fb, "D", "TRUE"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransaction("CLK")); //$NON-NLS-1$

		runTest(fb, seq, "SET"); //$NON-NLS-1$

		seq = newServiceSequence(fb);
		setVariable(fb, "D", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransaction("CLK", "EO", "Q:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		runTest(fb, seq, "SET"); //$NON-NLS-1$

		seq = newServiceSequence(fb);
		setVariable(fb, "D", "FALSE"); //$NON-NLS-1$ //$NON-NLS-2$
		addTransaction(seq, new FBTransaction("CLK")); //$NON-NLS-1$

		runTest(fb, seq, "RESET"); //$NON-NLS-1$
	}

	private static ServiceSequence newServiceSequence(final BasicFBType fb) {
		fb.getService().getServiceSequence().clear();
		return addServiceSequence(fb.getService());
	}


}
