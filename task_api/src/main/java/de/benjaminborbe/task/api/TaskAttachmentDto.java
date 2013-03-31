package de.benjaminborbe.task.api;

public class TaskAttachmentDto implements TaskAttachment {

	private String contentType;

	private String filename;

	private byte[] content;

	private String name;

	private TaskAttachmentIdentifier id;

	private TaskIdentifier task;

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public void setContent(final byte[] content) {
		this.content = content;
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

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public String getFilename() {
		return filename;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public void setTask(final TaskIdentifier task) {
		this.task = task;
	}

	@Override
	public TaskIdentifier getTask() {
		return task;
	}
}
