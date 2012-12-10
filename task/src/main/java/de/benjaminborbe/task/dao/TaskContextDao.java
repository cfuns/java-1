package de.benjaminborbe.task.dao;

import java.util.List;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.task.api.TaskContextIdentifier;

public interface TaskContextDao extends Dao<TaskContextBean, TaskContextIdentifier> {

	List<TaskContextBean> getAllByUser(UserIdentifier userIdentifier) throws StorageException;

	TaskContextBean findByName(UserIdentifier userIdentifier, String taskContextName) throws StorageException, EntityIteratorException;

}
