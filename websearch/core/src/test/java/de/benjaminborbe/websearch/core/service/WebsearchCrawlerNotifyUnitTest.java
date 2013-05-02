package de.benjaminborbe.websearch.core.service;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
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
		final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
		final WebsearchPageBean websearchPageBean = EasyMock.createMock(WebsearchPageBean.class);
		final Calendar calendar = EasyMock.createMock(Calendar.class);
		final HttpHeader httpHeader = EasyMock.createMock(HttpHeader.class);
		final HttpContent httpContent = EasyMock.createMock(HttpContent.class);
		final Long duration = 23L;

		EasyMock.expect(websearchPageDao.findOrCreate(url)).andReturn(websearchPageBean);
		EasyMock.expect(httpResponse.getUrl()).andReturn(url).times(2);

		EasyMock.expect(httpResponse.getReturnCode()).andReturn(returnCode);
		websearchPageBean.setReturnCode(returnCode);

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
