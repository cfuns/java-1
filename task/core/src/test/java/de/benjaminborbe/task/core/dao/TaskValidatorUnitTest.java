package de.benjaminborbe.task.core.dao;

import de.benjaminborbe.lib.validation.ValidationConstraintValidator;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.task.core.dao.task.TaskBean;
import de.benjaminborbe.task.core.dao.task.TaskValidator;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import org.junit.Test;
import org.slf4j.Logger;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

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
		return new TaskValidator(calendarUtil, v);
	}

	private TaskBean buildTask(final String name, final Integer start, final Integer due, final TaskFocus focus) {
		final TaskBean task = new TaskBean();
		task.setId(new TaskIdentifier("13"));
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
