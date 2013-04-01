package de.benjaminborbe.filestorage.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.api.ValidationResult;
import de.benjaminborbe.filestorage.api.FilestorageEntry;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.filestorage.api.FilestorageService;
import de.benjaminborbe.filestorage.api.FilestorageServiceException;
import de.benjaminborbe.filestorage.dao.FilestorageEntryBean;
import de.benjaminborbe.filestorage.dao.FilestorageEntryDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.tools.util.IdGenerator;
import de.benjaminborbe.tools.util.IdGeneratorUUID;
import de.benjaminborbe.tools.validation.ValidationExecutor;
import org.slf4j.Logger;

@Singleton
public class FilestorageServiceImpl implements FilestorageService {

	private final Logger logger;

	private final FilestorageEntryDao filestorageEntryDao;

	private final ValidationExecutor validationExecutor;

	private final IdGenerator<String> iIdGenerator;

	@Inject
	public FilestorageServiceImpl(final Logger logger, final FilestorageEntryDao filestorageEntryDao, final ValidationExecutor validationExecutor, final IdGeneratorUUID iIdGenerator) {
		this.logger = logger;
		this.filestorageEntryDao = filestorageEntryDao;
		this.validationExecutor = validationExecutor;
		this.iIdGenerator = iIdGenerator;
	}

	@Override
	public FilestorageEntryIdentifier createFilestorageEntryIdentifier(final String id) throws FilestorageServiceException {
		logger.debug("createFilestorageEntryIdentifier");
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new FilestorageEntryIdentifier(id);
		}
	}

	@Override
	public FilestorageEntryIdentifier createFilestorageEntry(final FilestorageEntry filestorageEntry) throws FilestorageServiceException, ValidationException {
		try {
			final FilestorageEntryBean bean = filestorageEntryDao.create();
			bean.setId(createFilestorageEntryIdentifier(iIdGenerator.nextId()));
			bean.setContent(filestorageEntry.getContent());
			bean.setContentType(filestorageEntry.getContentType());
			bean.setFilename(filestorageEntry.getFilename());

			final ValidationResult errors = validationExecutor.validate(bean);
			if (errors.hasErrors()) {
				logger.warn("FilestorageEntryBean " + errors.toString());
				throw new ValidationException(errors);
			}

			filestorageEntryDao.save(bean);

			return bean.getId();
		} catch (StorageException e) {
			throw new FilestorageServiceException(e);
		}
	}

	@Override
	public FilestorageEntry getFilestorageEntry(final FilestorageEntryIdentifier filestorageEntryIdentifier) throws FilestorageServiceException {
		try {
			return filestorageEntryDao.load(filestorageEntryIdentifier);
		} catch (StorageException e) {
			throw new FilestorageServiceException(e);
		}
	}

	@Override
	public void deleteFilestorageEntry(final FilestorageEntryIdentifier filestorageEntryIdentifier) throws FilestorageServiceException {
		try {
			filestorageEntryDao.delete(filestorageEntryIdentifier);
		} catch (StorageException e) {
			throw new FilestorageServiceException(e);
		}
	}
}
