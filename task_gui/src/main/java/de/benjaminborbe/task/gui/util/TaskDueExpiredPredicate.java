package de.benjaminborbe.task.gui.util;

import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.date.CalendarUtil;

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
		logger.debug(calendarUtil.toDateTimeString(task.getDue()) + " <= " + calendarUtil.toDateTimeString(today) + " return: " + result);
		return result;
	}
}
