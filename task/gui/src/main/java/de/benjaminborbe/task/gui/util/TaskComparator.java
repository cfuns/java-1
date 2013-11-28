package de.benjaminborbe.task.gui.util;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.util.ComparatorChain;

import javax.inject.Inject;

public class TaskComparator extends ComparatorChain<Task> {

	@SuppressWarnings("unchecked")
	@Inject
	public TaskComparator(final TaskNameComparator name, final TaskPrioComparator prio) {
		super(prio, name);
	}

}
