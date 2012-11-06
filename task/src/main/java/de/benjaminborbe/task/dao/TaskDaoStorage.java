package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.task.api.TaskIdentifier;

@Singleton
public class TaskDaoStorage extends DaoStorage<TaskBean, TaskIdentifier> implements TaskDao {

	private static final String COLUMN_FAMILY = "task";

	private final TaskContextManyToManyRelation taskContextManyToManyRelation;

	@Inject
	public TaskDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<TaskBean> beanProvider,
			final TaskBeanMapper mapper,
			final TaskIdentifierBuilder identifierBuilder,
			final TaskContextManyToManyRelation taskContextManyToManyRelation) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
		this.taskContextManyToManyRelation = taskContextManyToManyRelation;
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public List<TaskBean> getTasksNotCompleted(final UserIdentifier userIdentifier, final int limit) throws StorageException {
		try {
			final Predicate<TaskBean> p = Predicates.and(new TaskOwnerPredicate(userIdentifier), new TaskNotCompletedPredicate());
			final List<TaskBean> result = new ArrayList<TaskBean>();
			final EntityIterator<TaskBean> i = getEntityIterator();
			while (i.hasNext()) {
				final TaskBean task = i.next();
				if (p.apply(task)) {
					result.add(task);
				}
			}
			return result;
		}
		catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public List<TaskBean> getTasksCompleted(final UserIdentifier userIdentifier, final int limit) throws StorageException {
		try {
			final Predicate<TaskBean> p = Predicates.and(new TaskOwnerPredicate(userIdentifier), new TaskCompletedPredicate());
			final List<TaskBean> result = new ArrayList<TaskBean>();
			final EntityIterator<TaskBean> i = getEntityIterator();
			while (i.hasNext()) {
				final TaskBean task = i.next();
				if (p.apply(task)) {
					result.add(task);
				}
			}
			return result;
		}
		catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
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
		try {
			final Predicate<TaskBean> p = new TaskOwnerPredicate(userIdentifier);
			final List<TaskBean> result = new ArrayList<TaskBean>();
			final EntityIterator<TaskBean> i = getEntityIterator();
			while (i.hasNext()) {
				final TaskBean task = i.next();
				if (p.apply(task)) {
					result.add(task);
				}
			}
			return result;
		}
		catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public void delete(final TaskIdentifier id) throws StorageException {
		super.delete(id);
		taskContextManyToManyRelation.removeA(id);
	}

	@Override
	public void delete(final TaskBean entity) throws StorageException {
		super.delete(entity);
		taskContextManyToManyRelation.removeA(entity.getId());
	}

}
