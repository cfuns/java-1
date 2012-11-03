package de.benjaminborbe.task.dao;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.inject.Provider;

import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class TaskBeanMapperUnitTest {

	private TaskBeanMapper getTaskBeanMapper() {
		final Provider<TaskBean> taskBeanProvider = new Provider<TaskBean>() {

			@Override
			public TaskBean get() {
				return new TaskBean();
			}
		};
		final TimeZoneUtil timeZoneUtil = new TimeZoneUtilImpl();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final CalendarUtil calendarUtil = new CalendarUtilImpl(parseUtil, timeZoneUtil);
		return new TaskBeanMapper(taskBeanProvider, calendarUtil, parseUtil);
	}

	@Test
	public void testId() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String id = "1337";
		{
			final TaskBean bean = new TaskBean();
			bean.setId(new TaskIdentifier(id));
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(TaskBeanMapper.ID), id);
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(TaskBeanMapper.ID, id);
			final TaskBean bean = mapper.map(data);
			assertEquals(id, String.valueOf(bean.getId()));
		}
	}

	@Test
	public void testName() {
	}

	@Test
	public void testCompleted() {
	}

	@Test
	public void testDescription() {
	}

	@Test
	public void testOwner() {
	}

	@Test
	public void testCreated() {
	}

	@Test
	public void testModified() {
	}

	@Test
	public void testStart() {
	}

	@Test
	public void testDue() {
	}

	@Test
	public void testDuration() {
	}

}
