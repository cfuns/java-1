package de.benjaminborbe.task.core.dao.attachment;

import com.google.inject.Provider;
import de.benjaminborbe.task.core.util.MapperFilestorageEntryIdentifier;
import de.benjaminborbe.task.core.util.MapperTaskAttachmentIdentifier;
import de.benjaminborbe.task.core.util.MapperTaskIdentifier;
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class TaskAttachmentBeanMapperUnitTest {

	private final String fieldName;

	private final String fieldValue;

	public TaskAttachmentBeanMapperUnitTest(final String fieldName, final String fieldValue) {
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	@Parameterized.Parameters(name = "{index} - \"{0}\" = \"{1}\"")
	public static Collection<Object[]> generateData() {
		final List<Object[]> result = new ArrayList<>();
		result.add(new Object[]{TaskAttachmentBeanMapper.ID, "1337"});
		result.add(new Object[]{TaskAttachmentBeanMapper.CREATED, "123456"});
		result.add(new Object[]{TaskAttachmentBeanMapper.MODIFIED, "123456"});
		result.add(new Object[]{TaskAttachmentBeanMapper.NAME, "testFile"});
		result.add(new Object[]{TaskAttachmentBeanMapper.TASK, "42"});
		result.add(new Object[]{TaskAttachmentBeanMapper.FILE, "23"});
		return result;
	}

	private TaskAttachmentBeanMapper getTaskAttachmentBeanMapper() {
		final Provider<TaskAttachmentBean> beanProvider = new ProviderMock<>(TaskAttachmentBean.class);
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();

		final CurrentTime currentTime = EasyMock.createMock(CurrentTime.class);
		EasyMock.replay(currentTime);

		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final MapperCalendar mapperCalendar = new MapperCalendar(timeZoneUtil, calendarUtil, parseUtil);
		final MapperString mapperString = new MapperString();
		final MapperTaskAttachmentIdentifier mapperTaskAttachmentIdentifier = new MapperTaskAttachmentIdentifier();
		final MapperTaskIdentifier mapperTaskIdentifier = new MapperTaskIdentifier();
		final MapperFilestorageEntryIdentifier mapperFilestorageEntryIdentifier = new MapperFilestorageEntryIdentifier();

		return new TaskAttachmentBeanMapper(beanProvider, mapperTaskIdentifier, mapperTaskAttachmentIdentifier, mapperString, mapperCalendar, mapperFilestorageEntryIdentifier);
	}

	@Test
	public void testMaxRetryCounter() throws Exception {
		final TaskAttachmentBeanMapper mapper = getTaskAttachmentBeanMapper();
		final Map<String, String> inputData = new HashMap<>();
		inputData.put(fieldName, fieldValue);
		final TaskAttachmentBean bean = mapper.map(inputData);
		final Map<String, String> data = mapper.map(bean);
		assertThat(data.containsKey(fieldName), is(true));
		assertThat(data.get(fieldName), is(fieldValue));
	}

}
