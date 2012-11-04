package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.task.api.TaskIdentifier;

@Singleton
public class TaskDaoStorage extends DaoStorage<TaskBean, TaskIdentifier> implements TaskDao {

	private static final String COLUMN_FAMILY = "task";

	@Inject
	public TaskDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<TaskBean> beanProvider,
			final TaskBeanMapper mapper,
			final TaskIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public List<TaskBean> getTasksNotCompleted(final UserIdentifier userIdentifier, final int limit) throws StorageException {
		final Collection<TaskBean> tasks = Collections2.filter(getAll(), Predicates.and(new TaskOwnerPredicate(userIdentifier), new TaskNotCompletedPredicate()));
		return new ArrayList<TaskBean>(tasks);
	}

	@Override
	public List<TaskBean> getTasksCompleted(final UserIdentifier userIdentifier, final int limit) throws StorageException {
		final Collection<TaskBean> tasks = Collections2.filter(getAll(), Predicates.and(new TaskOwnerPredicate(userIdentifier), new TaskCompletedPredicate()));
		return new ArrayList<TaskBean>(tasks);
	}

	@Override
	public int getMaxPriority(final UserIdentifier userIdentifier) throws StorageException {
		int max = 0;
		for (final TaskBean task : getTasks(userIdentifier)) {
			if (task.getPriority() != null && task.getPriority() > max) {
				max = task.getPriority();
			}
		}
		return max;
	}

	@Override
	public List<TaskBean> getTasks(final UserIdentifier userIdentifier) throws StorageException {
		final Collection<TaskBean> tasks = Collections2.filter(getAll(), new TaskOwnerPredicate(userIdentifier));
		return new ArrayList<TaskBean>(tasks);
	}
}
