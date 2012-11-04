package de.benjaminborbe.task.util;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.util.ComparatorBase;

public class TaskPrioComparator extends ComparatorBase<Task, Integer> {

	@Override
	public Integer getValue(final Task o) {
		return o.getPriority();
	}
}
