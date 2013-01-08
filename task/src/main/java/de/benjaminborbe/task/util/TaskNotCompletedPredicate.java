package de.benjaminborbe.task.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.task.api.Task;

public class TaskNotCompletedPredicate<E extends Task> implements Predicate<E> {

	public TaskNotCompletedPredicate() {
	}

	@Override
	public boolean apply(final E task) {
		return task != null && !Boolean.TRUE.equals(task.getCompleted());
	}
}
