package de.benjaminborbe.task.core.dao.attachment;

import com.google.inject.Provider;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.api.FilestorageServiceException;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.storage.tools.StorageValueMap;
import de.benjaminborbe.task.api.TaskAttachmentIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TaskAttachmentDaoStorage extends DaoStorage<TaskAttachmentBean, TaskAttachmentIdentifier> implements TaskAttachmentDao {

	private static final String COLUMN_FAMILY = "task_attachment";

	private final Logger logger;

	private final FilestorageService filestorageService;

	@Inject
	public TaskAttachmentDaoStorage(
		final Logger logger,
		final FilestorageService filestorageService,
		final StorageService storageService,
		final Provider<TaskAttachmentBean> beanProvider,
		final TaskAttachmentBeanMapper mapper,
		final TaskAttachmentIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		this.logger = logger;
		this.filestorageService = filestorageService;
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

	@Override
	public void delete(final TaskIdentifier id) throws StorageException {
		try {
			final IdentifierIterator<TaskAttachmentIdentifier> iterator = getIdentifierIterator(id);
			while (iterator.hasNext()) {
				delete(iterator.next());
			}
		} catch (IdentifierIteratorException e) {
			throw new StorageException(e);
		}
	}

	@Override
	public void onPreDelete(final TaskAttachmentIdentifier id) throws StorageException {
		try {
			final TaskAttachmentBean bean = load(id);
			filestorageService.deleteFilestorageEntry(bean.getFile());
		} catch (FilestorageServiceException e) {
			throw new StorageException(e);
		}
	}
}
