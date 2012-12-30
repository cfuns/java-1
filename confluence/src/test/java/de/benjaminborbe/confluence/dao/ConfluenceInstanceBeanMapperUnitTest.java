package de.benjaminborbe.confluence.dao;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperBoolean;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperInteger;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class ConfluenceInstanceBeanMapperUnitTest {

	private ConfluenceInstanceBeanMapper getConfluenceInstanceBeanMapper() {
		final Provider<ConfluenceInstanceBean> confluenceInstanceBeanProvider = new ProviderMock<ConfluenceInstanceBean>(ConfluenceInstanceBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperUserIdentifier mapperUserIdentifier = new MapperUserIdentifier();
		final MapperBoolean mapperBoolean = new MapperBoolean(parseUtil);
		final MapperString mapperString = new MapperString();
		final MapperConfluenceInstanceIdentifier mapperConfluenceInstanceIdentifier = new MapperConfluenceInstanceIdentifier();
		final MapperLong mapperLong = new MapperLong(parseUtil);
		final MapperInteger mapperInteger = new MapperInteger(parseUtil);
		return new ConfluenceInstanceBeanMapper(confluenceInstanceBeanProvider, mapperConfluenceInstanceIdentifier, mapperUserIdentifier, mapperString, mapperLong, mapperBoolean,
				mapperInteger, mapperCalendar);
	}

	@Test
	public void testOwnerMapping() throws Exception {
		final ConfluenceInstanceBeanMapper mapper = getConfluenceInstanceBeanMapper();
		final UserIdentifier value = new UserIdentifier("1337");
		final String fieldname = "owner";
		{
			final ConfluenceInstanceBean bean = new ConfluenceInstanceBean();
			bean.setOwner(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final ConfluenceInstanceBean bean = mapper.map(data);
			assertEquals(value, bean.getOwner());
		}
	}
}
