package de.benjaminborbe.task.gui.util;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.tools.util.ComparatorBase;

public class TaskNameComparator extends ComparatorBase<Task, String> {

	@Override
	public String getValue(final Task o) {
		return o.getName();
	}
}
