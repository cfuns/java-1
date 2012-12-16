package de.benjaminborbe.checklist.dao;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Provider;

import de.benjaminborbe.checklist.api.ChecklistEntryIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class ChecklistEntryBeanMapperUnitTest {

	private ChecklistEntryBeanMapper getChecklistEntryBeanMapper() {
		final Provider<ChecklistEntryBean> taskBeanProvider = new Provider<ChecklistEntryBean>() {

			@Override
			public ChecklistEntryBean get() {
				return new ChecklistEntryBean();
			}
		};

		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		return new ChecklistEntryBeanMapper(taskBeanProvider, parseUtil, timeZoneUtil, calendarUtil);
	}

	@Test
	public void testId() throws Exception {
		final ChecklistEntryBeanMapper mapper = getChecklistEntryBeanMapper();
		final ChecklistEntryIdentifier value = new ChecklistEntryIdentifier("1337");
		final String fieldname = "id";
		{
			final ChecklistEntryBean bean = new ChecklistEntryBean();
			bean.setId(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final ChecklistEntryBean bean = mapper.map(data);
			assertEquals(value, bean.getId());
		}
	}
}
