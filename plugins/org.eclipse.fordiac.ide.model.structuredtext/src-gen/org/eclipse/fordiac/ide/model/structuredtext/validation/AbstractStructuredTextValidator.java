/*
 * generated by Xtext 2.24.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.validation;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.validation.AbstractDeclarativeValidator;

public abstract class AbstractStructuredTextValidator extends AbstractDeclarativeValidator {
	
	@Override
	protected List<EPackage> getEPackages() {
		List<EPackage> result = new ArrayList<EPackage>();
		result.add(org.eclipse.fordiac.ide.model.structuredtext.structuredText.StructuredTextPackage.eINSTANCE);
		result.add(EPackage.Registry.INSTANCE.getEPackage("org.eclipse.fordiac.ide.model.libraryElement"));
		return result;
	}
}
