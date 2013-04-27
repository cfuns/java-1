package de.benjaminborbe.task.gui.util;

import com.google.common.base.Predicate;
import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.TimeZone;

public class TaskDueExpiredPredicate implements Predicate<Task> {

	private final CalendarUtil calendarUtil;

	private final TimeZone timeZone;

	private final Logger logger;

	@Inject
	public TaskDueExpiredPredicate(final Logger logger, final CalendarUtil calendarUtil, final TimeZone timeZone) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZone = timeZone;
	}

	@Override
	public boolean apply(final Task task) {
		final Calendar today = calendarUtil.today(timeZone);
		final boolean result = task.getDue() != null && calendarUtil.isLT(task.getDue(), today);
		logger.trace(calendarUtil.toDateTimeString(task.getDue()) + " <= " + calendarUtil.toDateTimeString(today) + " return: " + result);
		return result;
	}
}
