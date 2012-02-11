package de.benjaminborbe.tools.html;

import static org.junit.Assert.assertEquals;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class HtmlUtilTest {

	@Test
	public void testEscapeHtml() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger);
		assertEquals("&amp;", htmlUtil.escapeHtml("&"));
	}

	@Test
	public void testUnescapeHtml() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger);
		assertEquals("&", htmlUtil.unescapeHtml("&amp;"));
	}

	@Test
	public void testParseLinks() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger);
		assertEquals(0, htmlUtil.parseLinks(null).size());
		assertEquals(0, htmlUtil.parseLinks("").size());
		assertEquals(0, htmlUtil.parseLinks("http://www.benjamin-borbe.de").size());
		assertEquals(1, htmlUtil.parseLinks("<a href=\"http://www.benjamin-borbe.de\">linkLabel</a>").size());
		assertEquals(0, htmlUtil.parseLinks("<a rel=\"nofollow\" href=\"http://www.benjamin-borbe.de\">linkLabel</a>").size());
		assertEquals(1, htmlUtil.parseLinks("<html><body><a href=\"http://www.benjamin-borbe.de\">linkLabel</a></body></html>").size());
		assertEquals(0, htmlUtil.parseLinks("<html><head><meta name=\"robots\" content=\"nofollow\"></head><body><a href=\"http://www.benjamin-borbe.de\">linkLabel</a></body></html>").size());
		assertEquals(0, htmlUtil.parseLinks("<html><head><meta name=\"robots\" content=\"nofollow,test\"></head><body><a href=\"http://www.benjamin-borbe.de\">linkLabel</a></body></html>").size());
	}

}
