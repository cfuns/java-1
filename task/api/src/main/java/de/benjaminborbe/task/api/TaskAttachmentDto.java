package de.benjaminborbe.task.api;

import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;

public class TaskAttachmentDto implements TaskAttachment {

	private String name;

	private TaskAttachmentIdentifier id;

	private TaskIdentifier task;

	private FilestorageEntryIdentifier file;

	public void setFile(final FilestorageEntryIdentifier file) {
		this.file = file;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setId(final TaskAttachmentIdentifier id) {
		this.id = id;
	}

	@Override
	public TaskAttachmentIdentifier getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setTask(final TaskIdentifier task) {
		this.task = task;
	}

	@Override
	public TaskIdentifier getTask() {
		return task;
	}

	@Override
	public FilestorageEntryIdentifier getFile() {
		return file;
	}

}
