package de.benjaminborbe.task.gui.util;

import java.util.Calendar;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.date.CalendarUtil;

public class TaskStartReadyPredicate implements Predicate<Task> {

	private final CalendarUtil calendarUtil;

	private final Logger logger;

	@Inject
	public TaskStartReadyPredicate(final Logger logger, final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public boolean apply(final Task task) {
		final Calendar now = calendarUtil.now();
		logger.trace(calendarUtil.toDateTimeString(task.getStart()) + " <= " + calendarUtil.toDateTimeString(now));
		return task.getStart() == null || calendarUtil.isLE(task.getStart(), now);
	}
}
