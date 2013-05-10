package de.benjaminborbe.websearch.core.util;

import de.benjaminborbe.httpdownloader.tools.HttpUtil;
import de.benjaminborbe.index.api.IndexService;
import de.benjaminborbe.tools.html.HtmlUtil;
import de.benjaminborbe.tools.stream.ChannelTools;
import de.benjaminborbe.tools.stream.StreamUtil;
import de.benjaminborbe.tools.util.StringUtil;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class WebsearchAddToSearchIndexUnitTest {

	@Test
	public void testExtractTitle() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final IndexService indexerService = EasyMock.createMock(IndexService.class);
		final HtmlUtil htmlUtil = EasyMock.createMock(HtmlUtil.class);
		final StringUtil stringUtil = EasyMock.createMock(StringUtil.class);

		final String title = "Foo Bar Title";
		final String content = "<html><title>" + title + "</title><body></body></html>";
		final ChannelTools channelTools = new ChannelTools();
		final StreamUtil streamUtil = new StreamUtil(channelTools);
		final HttpUtil httpUtil = new HttpUtil(logger, streamUtil);

		EasyMock.expect(stringUtil.shorten(title, 80)).andReturn(title);
		EasyMock.expect(htmlUtil.filterHtmlTages(title)).andReturn(title);

		final Object[] mocks = new Object[]{htmlUtil, stringUtil, indexerService, logger};
		EasyMock.replay(mocks);

		final WebsearchAddToSearchIndex websearchAddToSearchIndex = new WebsearchAddToSearchIndex(logger, indexerService, htmlUtil, stringUtil, httpUtil);
		assertThat(websearchAddToSearchIndex.extractTitle(content), is(title));

		EasyMock.verify(mocks);
	}
}
