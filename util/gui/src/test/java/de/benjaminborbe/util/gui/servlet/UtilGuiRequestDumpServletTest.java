package de.benjaminborbe.util.gui.servlet;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UtilGuiRequestDumpServletTest {

	@Test
	public void testRemoveTagEmpty() {
		assertThat(UtilGuiRemoveTagServlet.removeTag("", "span"), is(""));
		assertThat(UtilGuiRemoveTagServlet.removeTag(null, "span"), is(nullValue()));
	}

	@Test
	public void testRemoveTagNoRemove() {
		assertThat(UtilGuiRemoveTagServlet.removeTag("test", "span"), is("test"));
	}

	@Test
	public void testRemoveOpenTag() {
		assertThat(UtilGuiRemoveTagServlet.removeTag("foo<span>bar", "span"), is("foo bar"));
		assertThat(UtilGuiRemoveTagServlet.removeTag("foo<span id=\"baem\">bar", "span"), is("foo bar"));
	}

	@Test
	public void testRemoveCloseTag() {
		assertThat(UtilGuiRemoveTagServlet.removeTag("foo</span>bar", "span"), is("foo bar"));
	}
}
