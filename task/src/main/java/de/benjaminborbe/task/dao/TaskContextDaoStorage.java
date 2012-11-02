package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.task.api.TaskContextIdentifier;

@Singleton
public class TaskContextDaoStorage extends DaoStorage<TaskContextBean, TaskContextIdentifier> implements TaskContextDao {

	private static final String COLUMN_FAMILY = "task_context";

	@Inject
	public TaskContextDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<TaskContextBean> beanProvider,
			final TaskContextBeanMapper mapper,
			final TaskContextIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public List<TaskContextBean> getAllByUser(final UserIdentifier userIdentifier) throws StorageException {
		final Collection<TaskContextBean> tasks = Collections2.filter(getAll(), new TaskContextOwnerPredicate(userIdentifier));

		return new ArrayList<TaskContextBean>(tasks);
	}

}
