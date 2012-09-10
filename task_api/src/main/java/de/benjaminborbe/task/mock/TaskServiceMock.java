package de.benjaminborbe.task.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.task.api.TaskService;

@Singleton
public class TaskServiceMock implements TaskService {

	@Inject
	public TaskServiceMock() {
	}

	@Override
	public void execute() {
	}
}
