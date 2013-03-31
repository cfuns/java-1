package de.benjaminborbe.task.dao.attachment;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.Dao;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.task.api.TaskAttachmentIdentifier;
import de.benjaminborbe.task.api.TaskIdentifier;

public interface TaskAttachmentDao extends Dao<TaskAttachmentBean, TaskAttachmentIdentifier> {

	IdentifierIterator<TaskAttachmentIdentifier> getIdentifierIterator(TaskIdentifier taskIdentifier) throws StorageException;

	EntityIterator<TaskAttachmentBean> getEntityIterator(TaskIdentifier taskIdentifier) throws StorageException;
}
