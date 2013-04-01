package de.benjaminborbe.task.api;

public interface TaskAttachmentWithContent extends TaskAttachment {

	byte[] getContent();

	String getContentType();

	String getFilename();
}
