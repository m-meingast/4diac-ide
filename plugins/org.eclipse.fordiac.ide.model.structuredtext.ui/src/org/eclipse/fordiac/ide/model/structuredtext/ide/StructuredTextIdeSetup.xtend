/*
 * generated by Xtext 2.11.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.ide

import com.google.inject.Guice
import org.eclipse.xtext.util.Modules2
import org.eclipse.fordiac.ide.model.structuredtext.StructuredTextRuntimeModule
import org.eclipse.fordiac.ide.model.structuredtext.StructuredTextStandaloneSetup

/**
 * Initialization support for running Xtext languages as language servers.
 */
class StructuredTextIdeSetup extends StructuredTextStandaloneSetup {

	override createInjector() {
		Guice.createInjector(Modules2.mixin(new StructuredTextRuntimeModule, new StructuredTextIdeModule))
	}
	
}
