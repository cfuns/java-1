package de.benjaminborbe.tools.html;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

public class HtmlUtilImplUnitTest {

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
		assertEquals(0, htmlUtil.parseLinks("<html><head><meta name=\"robots\" content=\"nofollow\"></head><body><a href=\"http://www.benjamin-borbe.de\">linkLabel</a></body></html>")
				.size());
		assertEquals(0,
				htmlUtil.parseLinks("<html><head><meta name=\"robots\" content=\"nofollow,test\"></head><body><a href=\"http://www.benjamin-borbe.de\">linkLabel</a></body></html>").size());
	}

	@Test
	public void testFilterHtmlTages() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger);
		assertNull(htmlUtil.filterHtmlTages(null));
		assertEquals("", htmlUtil.filterHtmlTages(""));
		assertEquals("", htmlUtil.filterHtmlTages(" "));
		assertEquals("", htmlUtil.filterHtmlTages("<br/>"));
		assertEquals("", htmlUtil.filterHtmlTages(" <br/> "));
		assertEquals("Bla Hello World", htmlUtil.filterHtmlTages("<html><head><title>Bla</title></head><body><h1>Hello World</h1></body></html>"));
	}

	@Test
	public void testAddLinks() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger);

		// http
		assertEquals("", htmlUtil.addLinks(""));
		assertEquals("\n", htmlUtil.addLinks("\n"));
		assertEquals("<a href=\"http://www.heise.de/\">http://www.heise.de/</a>", htmlUtil.addLinks("http://www.heise.de/"));
		assertEquals(" <a href=\"http://www.heise.de/\">http://www.heise.de/</a> ", htmlUtil.addLinks(" http://www.heise.de/ "));
		assertEquals("\n<a href=\"http://www.heise.de/\">http://www.heise.de/</a>\n", htmlUtil.addLinks("\nhttp://www.heise.de/\n"));
		assertEquals("<a href=\"http://www.heise.de/\">http://www.heise.de/</a> <a href=\"http://www.google.de/\">http://www.google.de/</a>",
				htmlUtil.addLinks("http://www.heise.de/ http://www.google.de/"));

		// https
		assertEquals("<a href=\"https://www.heise.de/\">https://www.heise.de/</a>", htmlUtil.addLinks("https://www.heise.de/"));

	}
}
