package de.benjaminborbe.search.gui.util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class SearchGuiTermHighlighterUnitTest {

	@Test
	public void testHighlight() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final SearchGuiTermHighlighter u = new SearchGuiTermHighlighter(logger);
		assertEquals("<b>foo</b>", u.highlightSearchTerms("foo", Arrays.asList("foo")));
		assertEquals("<b>FoO</b>", u.highlightSearchTerms("FoO", Arrays.asList("foo")));
		assertEquals("<b>foo</b>", u.highlightSearchTerms("foo", Arrays.asList("fOO")));
		assertEquals("a<b>foo</b>b", u.highlightSearchTerms("afoob", Arrays.asList("foo")));
		assertEquals("<b>foo</b><b>bar</b>", u.highlightSearchTerms("foobar", Arrays.asList("foo", "bar")));
		assertEquals("<b>foo</b><b>bar</b>", u.highlightSearchTerms("foobar", Arrays.asList("foo", "bar")));
		assertEquals("foo <b>Bällen</b> bar", u.highlightSearchTerms("foo Bällen bar", Arrays.asList("bällen")));
	}
}
