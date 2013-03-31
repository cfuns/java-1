package de.benjaminborbe.filestorage.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.api.FilestorageServiceException;

@Singleton
public class FilestorageServiceMock implements FilestorageService {

	@Inject
	public FilestorageServiceMock() {
	}

	@Override
	public FilestorageEntryIdentifier createFilestorageEntry(final String id) throws FilestorageServiceException {
		return null;
	}
}
