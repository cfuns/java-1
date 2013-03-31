package de.benjaminborbe.task.api;

public interface TaskAttachment {

	TaskAttachmentIdentifier getId();

	String getName();

	byte[] getContent();

	String getFilename();

	String getContentType();

	TaskIdentifier getTask();
}
