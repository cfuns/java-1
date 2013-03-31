package de.benjaminborbe.task.api;

public interface TaskAttachment {

	TaskAttachmentIdentifier getId();

	String getName();

	TaskIdentifier getTask();
}
