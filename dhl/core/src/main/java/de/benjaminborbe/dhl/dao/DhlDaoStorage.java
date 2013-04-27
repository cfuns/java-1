package de.benjaminborbe.dhl.dao;

import com.google.inject.Provider;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DhlDaoStorage extends DaoStorage<DhlBean, DhlIdentifier> implements DhlDao {

	private static final String COLUMN_FAMILY = "dhl";

	@Inject
	public DhlDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<DhlBean> beanProvider,
		final DhlBeanMapper mapper,
		final DhlIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil
	) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
