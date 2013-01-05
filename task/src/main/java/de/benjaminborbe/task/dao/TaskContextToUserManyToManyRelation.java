package de.benjaminborbe.task.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.ManyToManyRelationStorage;
import de.benjaminborbe.task.api.TaskContextIdentifier;

public class TaskContextToUserManyToManyRelation extends ManyToManyRelationStorage<TaskContextIdentifier, UserIdentifier> {

	private static final String COLUMN_FAMILY = "task_context_user_relation";

	@Inject
	public TaskContextToUserManyToManyRelation(final Logger logger, final StorageService storageService) throws StorageException {
		super(logger, storageService);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
