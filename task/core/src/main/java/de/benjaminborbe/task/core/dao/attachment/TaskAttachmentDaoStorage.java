package de.benjaminborbe.task.core.dao.attachment;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.task.api.TaskAttachmentIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

@Singleton
public class TaskAttachmentDaoStorage extends DaoStorage<TaskAttachmentBean, TaskAttachmentIdentifier> implements TaskAttachmentDao {

	private static final String COLUMN_FAMILY = "task_attachment";

	private final Logger logger;

	@Inject
	public TaskAttachmentDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<TaskAttachmentBean> beanProvider,
		final TaskAttachmentBeanMapper mapper,
		final TaskAttachmentIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.logger = logger;
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public IdentifierIterator<TaskAttachmentIdentifier> getIdentifierIterator(final TaskIdentifier taskIdentifier) throws StorageException {
		logger.debug("get IdentifierIterator for task: " + taskIdentifier);
		return getIdentifierIterator(new StorageValueMap(getEncoding()).add(TaskAttachmentBeanMapper.TASK, taskIdentifier.getId()));
	}

	@Override
	public EntityIterator<TaskAttachmentBean> getEntityIterator(final TaskIdentifier taskIdentifier) throws StorageException {
		logger.debug("get EntityIterator for task: " + taskIdentifier);
		return getEntityIterator(new StorageValueMap(getEncoding()).add(TaskAttachmentBeanMapper.TASK, taskIdentifier.getId()));
	}
}
