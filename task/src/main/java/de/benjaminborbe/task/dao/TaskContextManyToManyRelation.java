package de.benjaminborbe.task.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.ManyToManyRelationStorage;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;

public class TaskContextManyToManyRelation extends ManyToManyRelationStorage<TaskIdentifier, TaskContextIdentifier> {

	private static final String COLUMN_FAMILY = "task_context_relation";

	@Inject
	public TaskContextManyToManyRelation(final Logger logger, final StorageService storageService) throws StorageException {
		super(logger, storageService);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
