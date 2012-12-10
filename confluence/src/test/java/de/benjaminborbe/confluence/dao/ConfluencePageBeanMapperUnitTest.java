package de.benjaminborbe.confluence.dao;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class ConfluencePageBeanMapperUnitTest {

	private ConfluencePageBeanMapper getConfluencePageBeanMapper() {
		final Provider<ConfluencePageBean> confluencePageBeanProvider = new Provider<ConfluencePageBean>() {

			@Override
			public ConfluencePageBean get() {
				return new ConfluencePageBean();
			}
		};

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		return new ConfluencePageBeanMapper(confluencePageBeanProvider, parseUtil, timeZoneUtil, calendarUtil);
	}

	@Test
	public void testOwnerMapping() throws Exception {
		final ConfluencePageBeanMapper mapper = getConfluencePageBeanMapper();
		final String value = "1337";
		final String fieldname = "pageId";
		{
			final ConfluencePageBean bean = new ConfluencePageBean();
			bean.setPageId(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final ConfluencePageBean bean = mapper.map(data);
			assertEquals(value, bean.getPageId());
		}
	}
}