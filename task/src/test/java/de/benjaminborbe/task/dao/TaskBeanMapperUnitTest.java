package de.benjaminborbe.task.dao;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.inject.Provider;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.date.TimeZoneUtilImpl;
import de.benjaminborbe.tools.mapper.MapException;
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
		return new TaskBeanMapper(taskBeanProvider, parseUtil, calendarUtil);
	}

	@Test
	public void testId() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final TaskIdentifier value = new TaskIdentifier("1337");
		final String fieldname = "id";
		{
			final TaskBean bean = new TaskBean();
			bean.setId(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final TaskBean bean = mapper.map(data);
			assertEquals(value, bean.getId());
		}
	}

	@Test
	public void testName() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String value = "name123";
		final String fieldname = "name";
		{
			final TaskBean bean = new TaskBean();
			bean.setName(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), value);
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, value);
			final TaskBean bean = mapper.map(data);
			assertEquals(value, bean.getName());
		}
	}

	@Test
	public void testCompleted() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String fieldname = "completed";
		final boolean value = true;
		{
			final TaskBean bean = new TaskBean();
			bean.setCompleted(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final TaskBean bean = mapper.map(data);
			assertEquals(value, bean.getCompleted());
		}
	}

	@Test
	public void testDescription() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String fieldname = "description";
		final String value = "blabla";
		{
			final TaskBean bean = new TaskBean();
			bean.setDescription(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final TaskBean bean = mapper.map(data);
			assertEquals(value, bean.getDescription());
		}
	}

	@Test
	public void testOwner() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String fieldname = "owner";
		final UserIdentifier value = new UserIdentifier("god");
		{
			final TaskBean bean = new TaskBean();
			bean.setOwner(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final TaskBean bean = mapper.map(data);
			assertEquals(value, bean.getOwner());
		}
	}

	@Test
	public void testDuration() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String fieldname = "duration";
		final Long value = 1337L;
		{
			final TaskBean bean = new TaskBean();
			bean.setDuration(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(data.get(fieldname), String.valueOf(value));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(value));
			final TaskBean bean = mapper.map(data);
			assertEquals(value, bean.getDuration());
		}
	}

	@Test
	public void testCreated() throws MapException {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String fieldname = "created";
		final Calendar value = Calendar.getInstance();
		final long ms = 1351942796l * 1000l;
		value.clear();
		value.setTimeInMillis(ms);
		assertEquals(ms, value.getTimeInMillis());
		{
			final TaskBean bean = new TaskBean();
			bean.setCreated(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(String.valueOf(ms), data.get(fieldname));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(ms));
			final TaskBean bean = mapper.map(data);
			assertEquals(value.getTimeInMillis(), bean.getCreated().getTimeInMillis());
		}
	}

	@Test
	public void testModified() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String fieldname = "modified";
		final Calendar value = Calendar.getInstance();
		final long ms = 1351942796l * 1000l;
		value.clear();
		value.setTimeInMillis(ms);
		assertEquals(ms, value.getTimeInMillis());
		{
			final TaskBean bean = new TaskBean();
			bean.setModified(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(String.valueOf(ms), data.get(fieldname));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(ms));
			final TaskBean bean = mapper.map(data);
			assertEquals(value.getTimeInMillis(), bean.getModified().getTimeInMillis());
		}
	}

	@Test
	public void testStart() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String fieldname = "start";
		final Calendar value = Calendar.getInstance();
		final long ms = 1351942796l * 1000l;
		value.clear();
		value.setTimeInMillis(ms);
		assertEquals(ms, value.getTimeInMillis());
		{
			final TaskBean bean = new TaskBean();
			bean.setStart(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(String.valueOf(ms), data.get(fieldname));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(ms));
			final TaskBean bean = mapper.map(data);
			assertEquals(value.getTimeInMillis(), bean.getStart().getTimeInMillis());
		}
	}

	@Test
	public void testDue() throws Exception {
		final TaskBeanMapper mapper = getTaskBeanMapper();
		final String fieldname = "due";
		final Calendar value = Calendar.getInstance();
		final long ms = 1351942796l * 1000l;
		value.clear();
		value.setTimeInMillis(ms);
		assertEquals(ms, value.getTimeInMillis());
		{
			final TaskBean bean = new TaskBean();
			bean.setDue(value);
			final Map<String, String> data = mapper.map(bean);
			assertEquals(String.valueOf(ms), data.get(fieldname));
		}
		{
			final Map<String, String> data = new HashMap<String, String>();
			data.put(fieldname, String.valueOf(ms));
			final TaskBean bean = mapper.map(data);
			assertEquals(value.getTimeInMillis(), bean.getDue().getTimeInMillis());
		}
	}

}
