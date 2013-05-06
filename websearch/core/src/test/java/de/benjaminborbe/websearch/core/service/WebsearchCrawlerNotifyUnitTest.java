package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.crawler.api.CrawlerNotifierResult;
import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.net.URL;
import java.util.Calendar;

public class WebsearchCrawlerNotifyUnitTest {

	@Test
	public void testPageAttributes() throws Exception {
		final URL url = new URL("http://www.benjamin-borbe.de");
		final Integer returnCode = 1337;
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		final CalendarUtil calendarUtil = EasyMock.createMock(CalendarUtil.class);
		final WebsearchPageDao websearchPageDao = EasyMock.createMock(WebsearchPageDao.class);
		final CrawlerNotifierResult httpResponse = EasyMock.createMock(CrawlerNotifierResult.class);
		final WebsearchPageBean websearchPageBean = EasyMock.createMock(WebsearchPageBean.class);
		final Calendar calendar = EasyMock.createMock(Calendar.class);
		final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
		final HttpContent httpContent = EasyMock.createMock(HttpContent.class);
		final Long duration = 23L;
		final long depth = 42l;
		final int timeout = 5000;

		EasyMock.expect(websearchPageDao.findOrCreate(url, depth, timeout)).andReturn(websearchPageBean);
		EasyMock.expect(httpResponse.getUrl()).andReturn(url).times(2);

		EasyMock.expect(httpResponse.getReturnCode()).andReturn(returnCode);
		websearchPageBean.setReturnCode(returnCode);

		EasyMock.expect(httpResponse.getDepth()).andReturn(depth);
		EasyMock.expect(httpResponse.getTimeout()).andReturn(timeout);

		EasyMock.expect(httpResponse.getHeader()).andReturn(httpHeader);
		websearchPageBean.setHeader(httpHeader);

		EasyMock.expect(httpResponse.getContent()).andReturn(httpContent);
		websearchPageBean.setContent(httpContent);

		EasyMock.expect(httpResponse.getDuration()).andReturn(duration);
		websearchPageBean.setDuration(duration);

		EasyMock.expect(calendarUtil.now()).andReturn(calendar);
		websearchPageBean.setLastVisit(calendar);

		websearchPageDao.save(websearchPageBean);

		final Object[] mocks = new Object[]{logger, calendarUtil, websearchPageDao, httpResponse, websearchPageBean, calendar, httpHeader};

		EasyMock.replay(mocks);

		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, calendarUtil, websearchPageDao);
		websearchCrawlerNotify.notifiy(httpResponse);

		EasyMock.verify(mocks);
	}
}
