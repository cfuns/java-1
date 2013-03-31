package de.benjaminborbe.filestorage.api;

public interface FilestorageService {

	FilestorageEntryIdentifier createFilestorageEntry(String id) throws FilestorageServiceException;
}
