package de.benjaminborbe.wiki.render;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import de.benjaminborbe.wiki.render.WikiConfluenceRenderer;

public class WikiConfluenceRendererUnitTest {

	@Test
	public void testConvertMarkupToHtml() {
		final WikiRenderer converter = new WikiConfluenceRenderer();
		assertEquals("", converter.render(""));
		assertEquals("bla", converter.render("bla"));

		for (final String tag : Arrays.asList("h1", "h2", "h3", "H1", "H2", "H3")) {

			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render(" " + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("  " + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("   " + tag + ". Headline"));

			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ".Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ".  Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ".   Headline"));

			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ". Headline"));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ". Headline "));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ". Headline  "));
			assertEquals("<" + tag.toLowerCase() + ">Headline</" + tag.toLowerCase() + ">", converter.render("" + tag + ". Headline   "));
		}

	}

}
