package de.benjaminborbe.lunch.dao;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.map.MapChain;

@Singleton
public class LunchUserSettingsDaoStorage extends DaoStorage<LunchUserSettingsBean, LunchUserSettingsIdentifier> implements LunchUserSettingsDao {

	private final class UserIterator implements IdentifierIterator<UserIdentifier> {

		private final IdentifierIterator<LunchUserSettingsIdentifier> i;

		private UserIterator(final IdentifierIterator<LunchUserSettingsIdentifier> i) {
			this.i = i;
		}

		@Override
		public boolean hasNext() throws IdentifierIteratorException {
			return i.hasNext();
		}

		@Override
		public UserIdentifier next() throws IdentifierIteratorException {
			return new UserIdentifier(i.next().getId());
		}
	}

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

	@Override
	public IdentifierIterator<UserIdentifier> getActivUserIdentifierIterator() throws StorageException {
		return new UserIterator(getIdentifierIterator(new MapChain<StorageValue, StorageValue>().add(buildValue("notificationActivated"), buildValue("true"))));
	}
}
