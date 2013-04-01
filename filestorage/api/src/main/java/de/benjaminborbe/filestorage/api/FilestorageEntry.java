package de.benjaminborbe.filestorage.api;

public interface FilestorageEntry {

	FilestorageEntryIdentifier getId();

	byte[] getContent();

	String getContentType();

	String getFilename();
}
