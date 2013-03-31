package de.benjaminborbe.task.api;

public interface TaskService extends TaskServiceTask, TaskServiceTaskContext, TaskServiceTaskAttachment {

	String PERMISSION = "task";

}
