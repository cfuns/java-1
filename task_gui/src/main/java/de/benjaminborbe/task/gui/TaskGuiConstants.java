package de.benjaminborbe.task.gui;

public interface TaskGuiConstants {

	String NAME = "task";

	String URL_TASK_SWAP_PRIO = "/task/swapprio";

	String URL_TASK_CREATE = "/task/create";

	String URL_TASK_UPDATE = "/task/update";

	String URL_TASK_COMPLETE = "/task/complete";

	String URL_TASK_DELETE = "/task/delete";

	String URL_TASK_UNCOMPLETE = "/task/uncomplete";

	String URL_TASKS_COMPLETED = "/tasks/completed";

	String URL_TASKS_UNCOMPLETED = "/tasks/uncompletd";

	String URL_TASKCONTEXT_LIST = "/taskcontext/list";

	String URL_TASKCONTEXT_DELETE = "/taskcontext/delete";

	String URL_TASKCONTEXT_CREATE = "/taskcontext/create";

	String PARAMETER_TASK_ID = "task_id";

	String PARAMETER_TASK_NAME = "task_name";

	String PARAMETER_TASK_DESCRIPTION = "task_description";

	String PARAMETER_TASKCONTEXT_ID = "taskcontext_id";

	String PARAMETER_TASKCONTEXT_NAME = "taskcontext_name";

	String PARAMETER_SELECTED_TASKCONTEXT_ID = "selected_taskcontext_id";

	String PARAMETER_TASK_LIMIT = "task_limit";

	int DEFAULT_TASK_LIMIT = 20;

	String PARAMETER_TASK_ID_A = "task_id_a";

	String PARAMETER_TASK_ID_B = "task_id_b";

}
