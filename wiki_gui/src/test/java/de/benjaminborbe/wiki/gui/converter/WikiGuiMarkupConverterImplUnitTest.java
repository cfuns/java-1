package de.benjaminborbe.wiki.gui.converter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class WikiGuiMarkupConverterImplUnitTest {

	@Test
	public void testConvertMarkupToHtml() {
		final WikiGuiMarkupConverter converter = new WikiGuiMarkupConverterImpl();
		assertEquals("", converter.convertMarkupToHtml(""));
		assertEquals("bla", converter.convertMarkupToHtml("bla"));

		for (final String tag : Arrays.asList("h1", "h2", "h3", "H1", "H2", "H3")) {

			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml(" " + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("  " + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("   " + tag + ". Headline"));

			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ".Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ".  Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ".   Headline"));

			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ". Headline "));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ". Headline  "));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.convertMarkupToHtml("" + tag + ". Headline   "));
		}

	}

}
