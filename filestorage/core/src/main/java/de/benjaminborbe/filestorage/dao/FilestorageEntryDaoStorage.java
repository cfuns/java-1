package de.benjaminborbe.filestorage.dao;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

@Singleton
public class FilestorageEntryDaoStorage extends DaoStorage<FilestorageEntryBean, FilestorageEntryIdentifier> implements FilestorageEntryDao {

	private static final String COLUMN_FAMILY = "filestorage_entry";

	@Inject
	public FilestorageEntryDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<FilestorageEntryBean> beanProvider,
		final FilestorageEntryBeanMapper mapper,
		final FilestorageEntryIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}
}

