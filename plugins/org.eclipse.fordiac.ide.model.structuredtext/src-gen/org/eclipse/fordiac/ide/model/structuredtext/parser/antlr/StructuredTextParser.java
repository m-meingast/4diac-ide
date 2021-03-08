/*
 * generated by Xtext 2.24.0
 */
package org.eclipse.fordiac.ide.model.structuredtext.parser.antlr;

import com.google.inject.Inject;
import org.eclipse.fordiac.ide.model.structuredtext.parser.antlr.internal.InternalStructuredTextParser;
import org.eclipse.fordiac.ide.model.structuredtext.services.StructuredTextGrammarAccess;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;

public class StructuredTextParser extends AbstractAntlrParser {

	@Inject
	private StructuredTextGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalStructuredTextParser createParser(XtextTokenStream stream) {
		return new InternalStructuredTextParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "StructuredTextAlgorithm";
	}

	public StructuredTextGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(StructuredTextGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
