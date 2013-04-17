package de.benjaminborbe.checklist.dao;

import com.google.inject.Provider;
import de.benjaminborbe.checklist.api.ChecklistListIdentifier;
import de.benjaminborbe.checklist.util.MapperListIdentifier;
import de.benjaminborbe.checklist.util.MapperUserIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.MapperCalendar;
import de.benjaminborbe.tools.mapper.MapperString;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;
import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ChecklistListBeanMapperUnitTest {

	private ChecklistListBeanMapper getChecklistListBeanMapper() {
		final Provider<ChecklistListBean> taskBeanProvider = new ProviderMock<>(ChecklistListBean.class);
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
		final MapperListIdentifier mapperListIdentifier = new MapperListIdentifier();
		return new ChecklistListBeanMapper(taskBeanProvider, mapperListIdentifier, mapperUserIdentifier, mapperString, mapperCalendar);
	}

	@Test
	public void testId() throws Exception {
		final ChecklistListBeanMapper mapper = getChecklistListBeanMapper();
		final ChecklistListIdentifier value = new ChecklistListIdentifier("1337");
		final String fieldname = "id";
		{
			final ChecklistListBean bean = new ChecklistListBean();
			bean.setId(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<>();
			data.put(fieldname, String.valueOf(value));
			final ChecklistListBean bean = mapper.map(data);
			assertEquals(value, bean.getId());
		}
	}
}
