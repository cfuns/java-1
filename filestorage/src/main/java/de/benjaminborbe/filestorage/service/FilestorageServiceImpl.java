package de.benjaminborbe.filestorage.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.api.FilestorageServiceException;
import org.slf4j.Logger;

@Singleton
public class FilestorageServiceImpl implements FilestorageService {

	private final Logger logger;

	@Inject
	public FilestorageServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public FilestorageEntryIdentifier createFilestorageEntry(final String id) throws FilestorageServiceException {
		logger.debug("createFilestorageEntry");
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new FilestorageEntryIdentifier(id);
		}
	}
}
