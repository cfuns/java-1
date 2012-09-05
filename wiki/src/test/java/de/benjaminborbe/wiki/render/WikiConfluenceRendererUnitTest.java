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

	@Test
	public void testLi() {
		final WikiRenderer converter = new WikiConfluenceRenderer();
		assertEquals("<ul><li>l1</li></ul>", converter.render("* l1"));
		assertEquals("<ul><li>l1</li></ul>", converter.render("* l1\n"));
		assertEquals("<ul><li>l1</li><li>l2</li></ul>", converter.render("* l1\n* l2"));
		assertEquals("<ul><li>l1</li><li>l2</li></ul>", converter.render("* l1\n* l2\n"));
		assertEquals("<ul><li>l1</li><li>l2</li><li>l3</li></ul>", converter.render("* l1\n* l2\n* l3"));
		assertEquals("<ul><li>l1</li><li>l2</li><li>l3</li></ul>", converter.render("* l1\n* l2\n* l3\n"));
	}

}
