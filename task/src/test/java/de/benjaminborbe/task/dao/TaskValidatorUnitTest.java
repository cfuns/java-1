package de.benjaminborbe.task.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Calendar;

import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.date.CalendarUtilImpl;
import de.benjaminborbe.tools.date.CurrentTime;
import de.benjaminborbe.tools.date.TimeZoneUtil;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.validation.ValidationConstraintValidator;

public class TaskValidatorUnitTest {

	@Test
	public void testDueGreaterThanStart() {
		final TimeZoneUtil timeZoneUtil = null;
		final ParseUtil parseUtil = null;
		final CurrentTime currentTime = null;
		final Logger logger = null;
		final CalendarUtil calendarUtil = new CalendarUtilImpl(logger, currentTime, parseUtil, timeZoneUtil);
		final ValidationConstraintValidator v = new ValidationConstraintValidator();
		final TaskValidator taskValidator = new TaskValidator(calendarUtil, v);

		{
			final TaskBean task = buildTask(1, 2);
			assertThat(task.getStart(), is(notNullValue()));
			assertThat(task.getDue(), is(notNullValue()));
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask(1, 1);
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask(null, 1);
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask(1, null);
			assertThat(taskValidator.validate(task).size(), is(0));
		}
		{
			final TaskBean task = buildTask(2, 1);
			assertThat(taskValidator.validate(task).size(), is(1));
		}
	}

	private TaskBean buildTask(final Integer start, final Integer due) {
		final TaskBean task = new TaskBean();
		task.setName("bla");
		task.setStart(buildCalendar(start));
		task.setDue(buildCalendar(due));
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
