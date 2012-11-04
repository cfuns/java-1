package de.benjaminborbe.task.dao;

import java.util.List;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.task.api.TaskIdentifier;

public interface TaskDao extends Dao<TaskBean, TaskIdentifier> {

	List<TaskBean> getTasks(UserIdentifier userIdentifier) throws StorageException;

	List<TaskBean> getTasksNotCompleted(UserIdentifier userIdentifier, int limit) throws StorageException;

	List<TaskBean> getTasksCompleted(UserIdentifier userIdentifier, int limit) throws StorageException;

	int getMaxPriority(UserIdentifier userIdentifier) throws StorageException;
}
