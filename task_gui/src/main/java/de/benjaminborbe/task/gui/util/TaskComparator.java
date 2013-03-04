package de.benjaminborbe.task.gui.util;

import com.google.inject.Inject;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.util.ComparatorChain;

public class TaskComparator extends ComparatorChain<Task> {

	@Inject
	public TaskComparator(final TaskNameComparator name, final TaskPrioComparator prio) {
		super(prio, name);
	}

}
