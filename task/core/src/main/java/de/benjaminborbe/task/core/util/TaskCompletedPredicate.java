package de.benjaminborbe.task.core.util;

import com.google.common.base.Predicate;
import de.benjaminborbe.task.api.Task;

public class TaskCompletedPredicate<E extends Task> implements Predicate<E> {

	public TaskCompletedPredicate() {
	}

	@Override
	public boolean apply(final E task) {
		return task != null && Boolean.TRUE.equals(task.getCompleted());
	}
}
