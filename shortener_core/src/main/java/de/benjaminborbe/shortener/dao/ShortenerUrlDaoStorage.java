package de.benjaminborbe.shortener.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.shortener.api.ShortenerUrlIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class ShortenerUrlDaoStorage extends DaoStorage<ShortenerUrlBean, ShortenerUrlIdentifier> implements ShortenerUrlDao {

	@Inject
	public ShortenerUrlDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<ShortenerUrlBean> beanProvider,
			final ShortenerUrlBeanMapper mapper,
			final ShortenerUrlIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "shortener_url";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
