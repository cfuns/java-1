package de.benjaminborbe.task.gui.util;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.date.CalendarUtil;

public class TaskDueExpiredPredicate implements Predicate<Task> {

	private final CalendarUtil calendarUtil;

	@Inject
	public TaskDueExpiredPredicate(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
	}

	@Override
	public boolean apply(final Task task) {
		return task.getDue() != null && calendarUtil.isLT(task.getDue(), calendarUtil.today());
	}
}
