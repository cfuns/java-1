package de.benjaminborbe.task.api;

public class TaskAttachmentWithContentDto extends TaskAttachmentDto implements TaskAttachmentWithContent {

	private String contentType;

	private String filename;

	private byte[] content;

	public String getContentType() {
		return contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}
}
