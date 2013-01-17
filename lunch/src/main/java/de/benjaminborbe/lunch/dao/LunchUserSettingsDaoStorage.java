package de.benjaminborbe.lunch.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class LunchUserSettingsDaoStorage extends DaoStorage<LunchUserSettingsBean, LunchUserSettingsIdentifier> implements LunchUserSettingsDao {

	@Inject
	public LunchUserSettingsDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<LunchUserSettingsBean> beanProvider,
			final LunchUserSettingsBeanMapper mapper,
			final LunchUserSettingsIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	private static final String COLUMN_FAMILY = "lunch_user_settings";

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public LunchUserSettingsBean findOrCreate(final LunchUserSettingsIdentifier id) throws StorageException {
		{
			final LunchUserSettingsBean bean = load(id);
			if (bean != null) {
				return bean;
			}
		}
		{
			final LunchUserSettingsBean bean = create();
			bean.setId(id);
			return bean;
		}
	}

	@Override
	public LunchUserSettingsBean findOrCreate(final UserIdentifier userIdentifier) throws StorageException {
		return findOrCreate(new LunchUserSettingsIdentifier(userIdentifier.getId()));
	}
}
