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
	public void testNotify() throws Exception {
		final String urlString = "http://www.benjamin-borbe.de";
		final URL url = new URL(urlString);

		final Calendar calendar = EasyMock.createMock(Calendar.class);
		EasyMock.replay(calendar);

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final CalendarUtil calendarUtil = EasyMock.createMock(CalendarUtil.class);
		EasyMock.expect(calendarUtil.now()).andReturn(calendar);
		EasyMock.replay(calendarUtil);

		final HttpHeader header = EasyMock.createMock(HttpHeader.class);
		EasyMock.replay(header);

		final HttpContent content = EasyMock.createMock(HttpContent.class);
		EasyMock.replay(content);

		final Integer returnCode = 1337;

		final HttpResponse httpResponse = EasyMock.createMock(HttpResponse.class);
		EasyMock.expect(httpResponse.getUrl()).andReturn(url).anyTimes();
		EasyMock.expect(httpResponse.getHeader()).andReturn(header).anyTimes();
		EasyMock.expect(httpResponse.getContent()).andReturn(content).anyTimes();
		EasyMock.expect(httpResponse.getReturnCode()).andReturn(returnCode).anyTimes();
		EasyMock.replay(httpResponse);

		final WebsearchPageBean websearchPageBean = EasyMock.createMock(WebsearchPageBean.class);
		websearchPageBean.setLastVisit(calendar);
		websearchPageBean.setHeader(header);
		websearchPageBean.setContent(content);
		websearchPageBean.setReturnCode(1337);
		EasyMock.replay(websearchPageBean);

		final WebsearchPageDao websearchPageDao = EasyMock.createMock(WebsearchPageDao.class);
		EasyMock.expect(websearchPageDao.findOrCreate(url)).andReturn(websearchPageBean);
		websearchPageDao.save(websearchPageBean);
		EasyMock.replay(websearchPageDao);

		final WebsearchCrawlerNotify websearchCrawlerNotify = new WebsearchCrawlerNotify(logger, calendarUtil, websearchPageDao);
		websearchCrawlerNotify.notifiy(httpResponse);
	}
}
