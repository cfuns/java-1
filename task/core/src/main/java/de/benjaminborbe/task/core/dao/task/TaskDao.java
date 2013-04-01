package de.benjaminborbe.task.core.dao.task;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskFocus;
import de.benjaminborbe.task.api.TaskIdentifier;

public interface TaskDao extends Dao<TaskBean, TaskIdentifier> {

	int getMaxPriority(UserIdentifier userIdentifier) throws StorageException;

	EntityIterator<TaskBean> getTasks(UserIdentifier userIdentifier) throws StorageException;

	EntityIterator<TaskBean> getTasks(UserIdentifier userIdentifier, boolean completed) throws StorageException;

	EntityIterator<TaskBean> getTasks(UserIdentifier userIdentifier, TaskFocus taskFocus, boolean completed) throws StorageException;

	EntityIterator<TaskBean> getTasks(TaskContextIdentifier taskContextIdentifier, boolean completed) throws StorageException;

	EntityIterator<TaskBean> getTasks(TaskContextIdentifier taskContextIdentifier, TaskFocus taskFocus, boolean completed) throws StorageException;

	EntityIterator<TaskBean> getTaskChilds(TaskIdentifier taskIdentifier) throws StorageException;

	EntityIterator<TaskBean> getTasks(TaskContextIdentifier taskContextIdentifier) throws StorageException;

}
