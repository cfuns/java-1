package de.benjaminborbe.task.dao;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.task.api.TaskContextIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.map.MapChain;

@Singleton
public class TaskContextDaoStorage extends DaoStorage<TaskContextBean, TaskContextIdentifier> implements TaskContextDao {

	private static final String COLUMN_FAMILY = "task_context";

	private final TaskContextManyToManyRelation taskContextManyToManyRelation;

	@Inject
	public TaskContextDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<TaskContextBean> beanProvider,
			final TaskContextBeanMapper mapper,
			final TaskContextIdentifierBuilder identifierBuilder,
			final TaskContextManyToManyRelation taskContextManyToManyRelation,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.taskContextManyToManyRelation = taskContextManyToManyRelation;
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public List<TaskContextBean> getAllByUser(final UserIdentifier userIdentifier) throws StorageException {
		try {
			final Predicate<TaskContextBean> p = new TaskContextOwnerPredicate(userIdentifier);
			final List<TaskContextBean> result = new ArrayList<TaskContextBean>();
			final EntityIterator<TaskContextBean> i = getEntityIterator();
			while (i.hasNext()) {
				final TaskContextBean task = i.next();
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
	public void delete(final TaskContextIdentifier id) throws StorageException {
		super.delete(id);
		taskContextManyToManyRelation.removeB(id);
	}

	@Override
	public void delete(final TaskContextBean entity) throws StorageException {
		super.delete(entity);
		taskContextManyToManyRelation.removeB(entity.getId());
	}

	@Override
	public TaskContextBean findByName(final UserIdentifier userIdentifier, final String taskContextName) throws StorageException, EntityIteratorException {
		final EntityIterator<TaskContextBean> i = getEntityIterator(new MapChain<String, String>().add(TaskContextBeanMapper.OWNER, String.valueOf(userIdentifier)).add(
				TaskContextBeanMapper.NAME, taskContextName));
		while (i.hasNext()) {
			return i.next();
		}
		return null;
	}
}
