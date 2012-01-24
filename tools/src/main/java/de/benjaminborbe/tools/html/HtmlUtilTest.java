package de.benjaminborbe.tools.html;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class HtmlUtilTest {

	@Test
	public void testEscapeHtml() {
		final HtmlUtil htmlUtil = new HtmlUtil();
		assertEquals("&amp;", htmlUtil.escapeHtml("&"));
	}

	@Test
	public void testUnescapeHtml() {
		final HtmlUtil htmlUtil = new HtmlUtil();
		assertEquals("&", htmlUtil.unescapeHtml("&amp;"));
	}

}
