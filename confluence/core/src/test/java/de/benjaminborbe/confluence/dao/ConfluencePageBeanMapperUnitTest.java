package de.benjaminborbe.confluence.dao;

import com.google.inject.Provider;
import de.benjaminborbe.confluence.util.MapperConfluenceInstanceIdentifier;
import de.benjaminborbe.confluence.util.MapperConfluencePageIdentifier;
import de.benjaminborbe.confluence.util.MapperUserIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.mapper.MapperUrl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConfluencePageBeanMapperUnitTest {

	private ConfluencePageBeanMapper getConfluencePageBeanMapper() {
		final Provider<ConfluencePageBean> confluencePageBeanProvider = new ProviderMock<ConfluencePageBean>(ConfluencePageBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperUserIdentifier mapperUserIdentifier = new MapperUserIdentifier();
		final MapperString mapperString = new MapperString();
		final MapperConfluenceInstanceIdentifier mapperConfluenceInstanceIdentifier = new MapperConfluenceInstanceIdentifier();
		final MapperConfluencePageIdentifier mapperConfluencePageIdentifier = new MapperConfluencePageIdentifier();
		final MapperUrl mapperUrl = new MapperUrl(parseUtil);
		return new ConfluencePageBeanMapper(confluencePageBeanProvider, mapperConfluencePageIdentifier, mapperUserIdentifier, mapperConfluenceInstanceIdentifier, mapperString,
			mapperCalendar, mapperUrl);
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
