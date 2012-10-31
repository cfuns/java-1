package de.benjaminborbe.task.dao;

import java.util.List;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.task.api.TaskIdentifier;

public interface TaskDao extends Dao<TaskBean, TaskIdentifier> {

	List<TaskBean> getNextTasks(UserIdentifier userIdentifier, int limit) throws StorageException;
}
