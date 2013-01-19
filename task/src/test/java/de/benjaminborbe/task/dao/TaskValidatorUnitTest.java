package de.benjaminborbe.task.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;

import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;

public class TaskValidatorUnitTest {

	@Test
	public void testValidName() throws Exception {
		final TaskValidator taskValidator = getValidator();
		{
			final TaskBean task = buildTask("bla", 1, 2, TaskFocus.INBOX);
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask("bla bla", 1, 2, TaskFocus.INBOX);
			assertThat(taskValidator.validate(task).size(), is(0));
		}
	}

	@Test
	public void testDueGreaterThanStart() {
		final TaskValidator taskValidator = getValidator();

		{
			final TaskBean task = buildTask("bla", 1, 2, TaskFocus.INBOX);
			assertThat(task.getStart(), is(notNullValue()));
			assertThat(task.getDue(), is(notNullValue()));
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask("bla", 1, 1, TaskFocus.INBOX);
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask("bla", null, 1, TaskFocus.INBOX);
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask("bla", 1, null, TaskFocus.INBOX);
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask("bla", 2, 1, TaskFocus.INBOX);
			assertThat(taskValidator.validate(task).size(), is(1));
		}
	}

	private TaskValidator getValidator() {
		final TimeZoneUtil timeZoneUtil = null;
		final ParseUtil parseUtil = null;
		final CurrentTime currentTime = null;
		final Logger logger = null;
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final ValidationConstraintValidator v = new ValidationConstraintValidator();
		final TaskValidator taskValidator = new TaskValidator(calendarUtil, v);
		return taskValidator;
	}

	private TaskBean buildTask(final String name, final Integer start, final Integer due, final TaskFocus focus) {
		final TaskBean task = new TaskBean();
		task.setName(name);
		task.setStart(buildCalendar(start));
		task.setDue(buildCalendar(due));
		task.setFocus(focus);
		return task;
	}

	private Calendar buildCalendar(final Integer days) {
		if (days == null) {
			return null;
		}
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return calendar;
	}
}
