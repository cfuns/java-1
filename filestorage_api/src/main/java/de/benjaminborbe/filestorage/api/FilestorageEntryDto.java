package de.benjaminborbe.filestorage.api;

public class FilestorageEntryDto implements FilestorageEntry {

	private FilestorageEntryIdentifier id;

	private byte[] content;

	private String contentType;

	private String filename;

	@Override
	public FilestorageEntryIdentifier getId() {
		return id;
	}

	public void setId(final FilestorageEntryIdentifier id) {
		this.id = id;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}

	@Override
	public String getFilename() {
		return filename;
	}
}
