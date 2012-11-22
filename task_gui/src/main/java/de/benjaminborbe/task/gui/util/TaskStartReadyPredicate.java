package de.benjaminborbe.task.gui.util;

import java.util.Calendar;
import java.util.TimeZone;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.date.CalendarUtil;

public class TaskStartReadyPredicate implements Predicate<Task> {

	private final CalendarUtil calendarUtil;

	private final Logger logger;

	private final TimeZone timeZone;

	@Inject
	public TaskStartReadyPredicate(final Logger logger, final CalendarUtil calendarUtil, final TimeZone timeZone) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
		this.timeZone = timeZone;
	}

	@Override
	public boolean apply(final Task task) {
		final Calendar now = calendarUtil.now(timeZone);
		final boolean result = task.getStart() == null || calendarUtil.isLE(task.getStart(), now);
		logger.info(calendarUtil.toDateTimeString(task.getStart()) + " <= " + calendarUtil.toDateTimeString(now) + " return: " + result);
		return result;
	}
}
