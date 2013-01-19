package de.benjaminborbe.task.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.task.api.Task;

public class TaskWithoutContextPredicate<E extends Task> implements Predicate<E> {

	public TaskWithoutContextPredicate() {
	}

	@Override
	public boolean apply(final E task) {
		return task != null && task.getContext() == null;
	}
}
