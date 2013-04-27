package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.api.HttpUtil;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.util.StringUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WebsearchAddToSearchIndexUnitTest {

	@Test
	public void testExtractTitle() throws Exception {
		final String title = "Foo Bar Title";
		final String content = "<html><title>" + title + "</title><body></body></html>";

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final IndexService indexerService = EasyMock.createMock(IndexService.class);
		EasyMock.replay(indexerService);

		final StringUtil stringUtil = EasyMock.createMock(StringUtil.class);
		EasyMock.expect(stringUtil.shorten(title, 80)).andReturn(title);
		EasyMock.replay(stringUtil);

		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		EasyMock.expect(htmlUtil.filterHtmlTages(title)).andReturn(title);
		EasyMock.replay(htmlUtil);

		final HttpUtil httpUtil = new HttpUtil();
		final WebsearchAddToSearchIndex websearchAddToSearchIndex = new WebsearchAddToSearchIndex(logger, indexerService, htmlUtil, stringUtil, httpUtil);
		assertThat(websearchAddToSearchIndex.extractTitle(content), is(title));
	}
}
