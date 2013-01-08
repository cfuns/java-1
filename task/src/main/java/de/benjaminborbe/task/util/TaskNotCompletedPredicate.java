package de.benjaminborbe.task.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.task.dao.TaskBean;

public class TaskNotCompletedPredicate implements Predicate<TaskBean> {

	public TaskNotCompletedPredicate() {
	}

	@Override
	public boolean apply(final TaskBean task) {
		return task != null && !Boolean.TRUE.equals(task.getCompleted());
	}
}
