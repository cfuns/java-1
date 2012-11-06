package de.benjaminborbe.task.gui.util;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.task.api.Task;
import de.benjaminborbe.task.api.TaskIdentifier;

public class TaskGuiUtil {

	@Inject
	public TaskGuiUtil() {
		super();
	}

	public List<Task> getChildTasks(final List<Task> allTasks, final TaskIdentifier parentId) {
		final List<Task> result = new ArrayList<Task>();
		for (final Task task : allTasks) {
			if (task.getParentId() == null && parentId == null || task.getParentId() != null && parentId != null && task.getParentId().equals(parentId)) {
				result.add(task);
			}
		}
		return result;
	}

}
