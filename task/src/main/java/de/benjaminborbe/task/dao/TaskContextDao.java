package de.benjaminborbe.task.dao;

import java.util.Collection;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.task.api.TaskContextIdentifier;

public interface TaskContextDao extends Dao<TaskContextBean, TaskContextIdentifier> {

	Collection<TaskContextBean> getTaskContextsByUser(UserIdentifier userIdentifier) throws StorageException;

	TaskContextBean findByName(UserIdentifier userIdentifier, String taskContextName) throws StorageException;

	Collection<TaskContextIdentifier> getTaskContextIdentifiersByUser(UserIdentifier userIdentifier) throws StorageException;

}
