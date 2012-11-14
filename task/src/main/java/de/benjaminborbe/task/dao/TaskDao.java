package de.benjaminborbe.task.dao;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.task.api.TaskIdentifier;

public interface TaskDao extends Dao<TaskBean, TaskIdentifier> {

	EntityIterator<TaskBean> getTasks(UserIdentifier userIdentifier) throws StorageException;

	EntityIterator<TaskBean> getTasksNotCompleted(UserIdentifier userIdentifier) throws StorageException;

	EntityIterator<TaskBean> getTasksCompleted(UserIdentifier userIdentifier) throws StorageException;

	int getMaxPriority(UserIdentifier userIdentifier) throws StorageException;

	EntityIterator<TaskBean> getTaskChilds(UserIdentifier userIdentifier, TaskIdentifier taskIdentifier) throws StorageException;
}
