package de.benjaminborbe.dhl.status;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperLong;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class DhlBeanMapperUnitTest {

	private DhlBeanMapper getDhlBeanMapper() {
		final Provider<DhlBean> provider = new ProviderMock<DhlBean>(DhlBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);

		final MapperLong mapperLong = new MapperLong(parseUtil);
		final MapperDhlIdentifier mapperDhlIdentifier = new MapperDhlIdentifier();
		return new DhlBeanMapper(provider, mapperLong, mapperCalendar, mapperDhlIdentifier);
	}

	@Test
	public void testId() throws Exception {
		final DhlBeanMapper mapper = getDhlBeanMapper();
		final DhlIdentifier value = new DhlIdentifier("1337");
		final String fieldname = "id";
		{
			final DhlBean bean = new DhlBean();
			bean.setId(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final DhlBean bean = mapper.map(data);
			assertEquals(value, bean.getId());
		}
	}

}
