package de.benjaminborbe.filestorage.dao;

import de.benjaminborbe.filestorage.api.FilestorageEntry;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.Calendar;

public class FilestorageEntryBean extends EntityBase<FilestorageEntryIdentifier> implements FilestorageEntry, HasCreated, HasModified {

	private static final long serialVersionUID = 6058606350883201939L;

	private FilestorageEntryIdentifier id;

	private Calendar created;

	private Calendar modified;

	private byte[] content;

	private String filename;

	private String contentType;

	@Override
	public FilestorageEntryIdentifier getId() {
		return id;
	}

	public void setId(final FilestorageEntryIdentifier id) {
		this.id = id;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(final Calendar created) {
		this.created = created;
	}

	public Calendar getModified() {
		return modified;
	}

	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public String getFilename() {
		return filename;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	public void setFilename(final String filename) {
		this.filename = filename;
	}
}
