package de.benjaminborbe.task.api;

import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;

public interface TaskAttachment {

	TaskAttachmentIdentifier getId();

	String getName();

	TaskIdentifier getTask();

	FilestorageEntryIdentifier getFile();
}
