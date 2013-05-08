package de.benjaminborbe.tools.html;

import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.ResourceUtil;
import de.benjaminborbe.tools.util.ResourceUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class HtmlUtilImplUnitTest {

	@Test
	public void testEscapeHtml() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		assertEquals("&amp;", htmlUtil.escapeHtml("&"));
	}

	@Test
	public void testUnescapeHtml() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		assertEquals("&", htmlUtil.unescapeHtml("&amp;"));
	}

	@Test
	public void testParseLinks() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
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
	public void testParseLinksImgSrc() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);

		assertThat(htmlUtil.parseLinks("<img src=\"http://www.benjamin-borbe.de/images/logo.jpg\">"), is(notNullValue()));
		assertThat(htmlUtil.parseLinks("<img src=\"http://www.benjamin-borbe.de/images/logo.jpg\">").size(), is(1));
		assertThat(htmlUtil.parseLinks("<img src=\"http://www.benjamin-borbe.de/images/logo.jpg\">"), is(hasItem("http://www.benjamin-borbe.de/images/logo.jpg")));

		assertThat(htmlUtil.parseLinks("<img src=\"http://www.benjamin-borbe.de/images/logo.jpg\" />"), is(notNullValue()));
		assertThat(htmlUtil.parseLinks("<img src=\"http://www.benjamin-borbe.de/images/logo.jpg\" />").size(), is(1));
		assertThat(htmlUtil.parseLinks("<img src=\"http://www.benjamin-borbe.de/images/logo.jpg\" />"), is(hasItem("http://www.benjamin-borbe.de/images/logo.jpg")));

		assertThat(htmlUtil.parseLinks("<img src_o=\"http://www.benjamin-borbe.de/images/logo.jpg\">"), is(notNullValue()));
		assertThat(htmlUtil.parseLinks("<img src_o=\"http://www.benjamin-borbe.de/images/logo.jpg\">").size(), is(1));
		assertThat(htmlUtil.parseLinks("<img src_o=\"http://www.benjamin-borbe.de/images/logo.jpg\">"), is(hasItem("http://www.benjamin-borbe.de/images/logo.jpg")));
	}

	@Test
	public void testFilterHtmlTages() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		assertNull(htmlUtil.filterHtmlTages(null));
		assertEquals("", htmlUtil.filterHtmlTages(""));
		assertEquals("ü", htmlUtil.filterHtmlTages("&uuml;"));
		assertEquals("", htmlUtil.filterHtmlTages(" "));
		assertEquals("", htmlUtil.filterHtmlTages("<br/>"));
		assertEquals("", htmlUtil.filterHtmlTages(" <br/> "));
		assertEquals("Bla Hello World", htmlUtil.filterHtmlTages("<html><head><title>Bla</title></head><body><h1>Hello World</h1></body></html>"));
		assertEquals("Bla Hello World", htmlUtil.filterHtmlTages("<html><head><title>Bla</title></head><body><h1>Hello World<!-- comment --></h1></body></html>"));
		assertEquals("Bla Hello World", htmlUtil.filterHtmlTages("<html><head><title>Bla</title></head><body><h1>Hello World<script>// comment</script></h1></body></html>"));
		assertEquals("Bla Hello World", htmlUtil.filterHtmlTages("<html><head><title>Bla</title></head><body><style/><h1>Hello World<style>// comment</style></h1></body></html>"));
		assertEquals("", htmlUtil.filterHtmlTages("<script bla />"));
		assertEquals("", htmlUtil.filterHtmlTages("<script>bla</script>"));
		assertEquals("", htmlUtil.filterHtmlTages("<script type=\"text/javascript\">bla</script>"));
		assertEquals("hello", htmlUtil.filterHtmlTages("<script foo>bla</script><a>\nhello\n</a>"));
	}

	@Test
	public void testScript() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		assertEquals(
			"bin/nodetool -host 192.168.0.103 drain",
			htmlUtil
				.filterHtmlTages("<script type=\"syntaxhighlighter\" class=\"theme: Confluence; brush: java; gutter: false\"><![CDATA[bin/nodetool -host 192.168.0.103 drain]]></script>"));
	}

	@Test
	public void testFilterHtmlTagesSample() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final StreamUtil streamUtil = new StreamUtil(new ChannelTools());
		final ResourceUtil resourceUtil = new ResourceUtilImpl(streamUtil);
		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);
		final String htmlContent = resourceUtil.getResourceContentAsString("sample.html");
		assertEquals("Home", htmlUtil.filterHtmlTages(htmlContent));
	}

	@Test
	public void testAddLinks() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final HtmlTagParser htmlTagParser = new HtmlTagParser(logger);
		final HtmlUtil htmlUtil = new HtmlUtilImpl(logger, htmlTagParser);

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
