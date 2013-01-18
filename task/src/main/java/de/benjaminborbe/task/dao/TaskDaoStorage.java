package de.benjaminborbe.task.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class TaskDaoStorage extends DaoStorage<TaskBean, TaskIdentifier> implements TaskDao {

	private static final String COLUMN_FAMILY = "task";

	private final Logger logger;

	@Inject
	public TaskDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<TaskBean> beanProvider,
			final TaskBeanMapper mapper,
			final TaskIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.logger = logger;
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public EntityIterator<TaskBean> getTasksNotCompleted(final UserIdentifier userIdentifier) throws StorageException {
		logger.trace("getTasksNotCompleted for user: " + userIdentifier);
		return getEntityIterator(new StorageValueMap(getEncoding()).add("owner", String.valueOf(userIdentifier)).add("completed", "false"));
	}

	@Override
	public EntityIterator<TaskBean> getTasksCompleted(final UserIdentifier userIdentifier) throws StorageException {
		logger.trace("getTasksCompleted for user: " + userIdentifier);
		return getEntityIterator(new StorageValueMap(getEncoding()).add("owner", String.valueOf(userIdentifier)).add("completed", "true"));
	}

	@Override
	public int getMaxPriority(final UserIdentifier userIdentifier) throws StorageException {
		try {
			int max = 0;
			final EntityIterator<TaskBean> i = getTasks(userIdentifier);
			while (i.hasNext()) {
				final TaskBean task = i.next();
				if (task.getPriority() != null && task.getPriority() > max) {
					max = task.getPriority();
				}
			}
			return max;
		}
		catch (final EntityIteratorException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public EntityIterator<TaskBean> getTasks(final UserIdentifier userIdentifier) throws StorageException {
		return getEntityIterator(new StorageValueMap(getEncoding()).add("owner", String.valueOf(userIdentifier)));
	}

	@Override
	public EntityIterator<TaskBean> getTaskChilds(final TaskIdentifier taskIdentifier) throws StorageException {
		return getEntityIterator(new StorageValueMap(getEncoding()).add("parentId", String.valueOf(taskIdentifier)));
	}
}
