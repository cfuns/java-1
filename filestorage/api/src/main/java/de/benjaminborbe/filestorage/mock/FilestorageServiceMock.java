package de.benjaminborbe.filestorage.mock;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.filestorage.api.FilestorageEntry;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.api.FilestorageServiceException;

public class FilestorageServiceMock implements FilestorageService {

	public FilestorageServiceMock() {
	}

	@Override
	public FilestorageEntryIdentifier createFilestorageEntryIdentifier(final String id) throws FilestorageServiceException {
		return null;
	}

	@Override
	public FilestorageEntryIdentifier createFilestorageEntry(final FilestorageEntry filestorageEntry) throws FilestorageServiceException, ValidationException {
		return null;
	}

	@Override
	public FilestorageEntry getFilestorageEntry(final FilestorageEntryIdentifier filestorageEntryIdentifier) throws FilestorageServiceException {
		return null;
	}

	@Override
	public void deleteFilestorageEntry(final FilestorageEntryIdentifier filestorageEntryIdentifier) throws FilestorageServiceException {
	}
}
