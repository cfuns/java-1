package de.benjaminborbe.task.dao;

import com.google.common.base.Predicate;

public class TaskCompletedPredicate implements Predicate<TaskBean> {

	public TaskCompletedPredicate() {
	}

	@Override
	public boolean apply(final TaskBean task) {
		return task != null && task.getCompleted();
	}
}
