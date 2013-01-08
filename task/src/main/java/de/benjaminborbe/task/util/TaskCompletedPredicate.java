package de.benjaminborbe.task.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.task.dao.TaskBean;

public class TaskCompletedPredicate implements Predicate<TaskBean> {

	public TaskCompletedPredicate() {
	}

	@Override
	public boolean apply(final TaskBean task) {
		return task != null && task.getCompleted();
	}
}
