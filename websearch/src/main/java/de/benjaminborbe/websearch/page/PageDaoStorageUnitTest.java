package de.benjaminborbe.websearch.page;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.net.URL;
import java.util.Date;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.mock.StorageServiceMock;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.DateUtil;
import de.benjaminborbe.tools.date.DateUtilImpl;

public class PageDaoStorageUnitTest {

	@Test
	public void testCrud() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);
		final StorageService b = new StorageServiceMock(logger);
		final Provider<PageBean> c = new Provider<PageBean>() {

			@Override
			public PageBean get() {
				return new PageBean();
			}
		};
		final PageDaoSubPagesAction d = new PageDaoSubPagesAction();
		final DateUtil f = new DateUtilImpl();
		final PageBeanMapper e = new PageBeanMapper(c, f);
		final PageIdentifierBuilder g = null;

		final CalendarUtil calendarUtil = EasyMock.createMock(CalendarUtil.class);
		EasyMock.replay(calendarUtil);

		final Date date = f.parseDateTime("2012-12-24 20:15:59");
		final PageDaoStorage dao = new PageDaoStorage(logger, b, c, d, e, g, calendarUtil);
		final URL url = new URL("http://www.heise.de");

		// create
		{
			final PageBean bean = dao.create();
			bean.setLastVisit(date);
			bean.setUrl(url);
			dao.save(bean);
		}

		// load
		{
			final PageBean bean = dao.load(url);
			assertEquals(date, bean.getLastVisit());
			assertEquals(url, bean.getUrl());
		}

		// delete
		{
			final PageBean bean = dao.load(url);
			dao.delete(bean);
		}

		// load
		{
			assertNull(dao.load(url));
		}
	}
}
