package de.benjaminborbe.websearch.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.TimeZone;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.mock.StorageServiceMock;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.CurrentTimeImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import de.benjaminborbe.websearch.api.WebsearchPageIdentifier;

public class WebsearchPageDaoStorageUnitTest {

	@Test
	public void testCrud() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final StorageService storageService = new StorageServiceMock(logger);
		final Provider<WebsearchPageBean> provider = new ProviderMock<WebsearchPageBean>(WebsearchPageBean.class);
		final WebsearchPageDaoSubPagesAction websearchPageDaoSubPagesAction = new WebsearchPageDaoSubPagesAction();
		final MapperWebsearchPageIdentifier mapperWebsearchPageIdentifier = new MapperWebsearchPageIdentifier();
		final CurrentTime currentTime = new CurrentTimeImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final WebsearchPageBeanMapper websearchPageBeanMapper = new WebsearchPageBeanMapper(provider, mapperCalendar, mapperWebsearchPageIdentifier);
		final WebsearchPageIdentifierBuilder websearchPageIdentifierBuilder = null;

		final TimeZone timeZone = timeZoneUtil.getUTCTimeZone();
		final Calendar calendar = calendarUtil.parseDateTime(timeZone, "2012-12-24 20:15:59");
		final WebsearchPageDaoStorage dao = new WebsearchPageDaoStorage(logger, storageService, provider, websearchPageDaoSubPagesAction, websearchPageBeanMapper,
				websearchPageIdentifierBuilder, calendarUtil);
		final String url = "http://www.heise.de";
		final WebsearchPageIdentifier websearchPageIdentifier = new WebsearchPageIdentifier(url);
		// create
		{
			final WebsearchPageBean bean = dao.create();
			bean.setLastVisit(calendar);
			bean.setUrl(url);
			dao.save(bean);
		}

		// load
		{
			final WebsearchPageBean bean = dao.load(websearchPageIdentifier);
			assertEquals(calendar, bean.getLastVisit());
			assertEquals(url, bean.getUrl());
		}

		// delete
		{
			final WebsearchPageBean bean = dao.load(websearchPageIdentifier);
			dao.delete(bean);
		}

		// load
		{
			assertNull(dao.load(websearchPageIdentifier));
		}
	}
}
