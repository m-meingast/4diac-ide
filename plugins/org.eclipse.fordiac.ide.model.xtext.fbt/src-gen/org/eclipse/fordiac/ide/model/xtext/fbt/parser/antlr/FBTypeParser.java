/*
 * generated by Xtext 2.11.0
 */
package org.eclipse.fordiac.ide.model.xtext.fbt.parser.antlr;

import com.google.inject.Inject;
import org.eclipse.fordiac.ide.model.xtext.fbt.parser.antlr.internal.InternalFBTypeParser;
import org.eclipse.fordiac.ide.model.xtext.fbt.services.FBTypeGrammarAccess;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;

public class FBTypeParser extends AbstractAntlrParser {

	@Inject
	private FBTypeGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalFBTypeParser createParser(XtextTokenStream stream) {
		return new InternalFBTypeParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "LibraryElement";
	}

	public FBTypeGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(FBTypeGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
