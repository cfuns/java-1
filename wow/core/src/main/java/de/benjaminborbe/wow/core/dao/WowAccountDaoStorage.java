package de.benjaminborbe.wow.core.dao;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;
import org.slf4j.Logger;

@Singleton
public class WowAccountDaoStorage extends DaoStorage<WowAccountBean, WowAccountIdentifier> implements WowAccountDao {

	private static final String COLUMN_FAMILY = "wow_account";

	@Inject
	public WowAccountDaoStorage(
		final Logger logger,
		final StorageService storageService,
		final Provider<WowAccountBean> beanProvider,
		final WowAccountBeanMapper mapper,
		final WowAccountIdentifierBuilder identifierBuilder,
		final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
