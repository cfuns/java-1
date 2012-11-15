package de.benjaminborbe.task.dao;

import com.google.common.base.Predicate;

public class TaskNotCompletedPredicate implements Predicate<TaskBean> {

	public TaskNotCompletedPredicate() {
	}

	@Override
	public boolean apply(final TaskBean task) {
		return task != null && !Boolean.TRUE.equals(task.getCompleted());
	}
}
