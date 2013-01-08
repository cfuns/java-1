package de.benjaminborbe.task.util;

import com.google.common.base.Predicate;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskFocus;

public class TaskFocusPredicate implements Predicate<Task> {

	private final TaskFocus taskFocus;

	public TaskFocusPredicate(final TaskFocus taskFocus) {
		this.taskFocus = taskFocus;
	}

	@Override
	public boolean apply(final Task task) {
		return taskFocus.equals(task.getFocus());
	}

}
