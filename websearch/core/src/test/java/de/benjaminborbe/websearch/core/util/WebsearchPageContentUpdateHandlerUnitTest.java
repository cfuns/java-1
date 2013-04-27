package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpHeaderDto;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.httpdownloader.api.HttpUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.net.URL;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WebsearchPageContentUpdateHandlerUnitTest {

	@Test
	public void testIsIndexAble() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final ParseUtil parseUtil = EasyMock.createMock(ParseUtil.class);
		EasyMock.replay(parseUtil);

		final HttpUtil httpUtil = new HttpUtil();

		final WebsearchAddToSearchIndex websearchAddToSearchIndex = EasyMock.createMock(WebsearchAddToSearchIndex.class);
		EasyMock.replay(websearchAddToSearchIndex);

		final WebsearchParseLinks websearchParseLinks = EasyMock.createMock(WebsearchParseLinks.class);
		EasyMock.replay(websearchParseLinks);

		final WebsearchPageContentUpdateHandler websearchCrawlerNotify = new WebsearchPageContentUpdateHandler(logger, httpUtil, websearchAddToSearchIndex, websearchParseLinks);

		assertTrue(websearchCrawlerNotify.isHtmlPage(createResult(true, "text/html")));
		assertTrue(websearchCrawlerNotify.isHtmlPage(createResult(true, "text/html;charset=UTF-8")));
		assertFalse(websearchCrawlerNotify.isHtmlPage(createResult(true, "text/plain")));
		assertFalse(websearchCrawlerNotify.isHtmlPage(createResult(false, "text/html")));
		assertFalse(websearchCrawlerNotify.isHtmlPage(createResult(false, "text/plain")));
		assertFalse(websearchCrawlerNotify.isHtmlPage(createResult(true, null)));
	}

	protected HttpResponse createResult(final boolean avaiable, final String contentType) {
		return new HttpResponse() {

			@Override
			public Integer getReturnCode() {
				return avaiable ? 200 : 400;
			}

			@Override
			public URL getUrl() {
				return null;
			}

			@Override
			public Long getDuration() {
				return null;
			}

			@Override
			public HttpHeader getHeader() {
				final HttpHeaderDto httpHeader = new HttpHeaderDto();
				httpHeader.setHeader("Content-Type", contentType);
				return httpHeader;
			}

			@Override
			public HttpContent getContent() {
				return null;
			}
		};
	}
}
