package de.benjaminborbe.poker.player;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.poker.api.PokerPlayerIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class PokerPlayerDaoStorage extends DaoStorage<PokerPlayerBean, PokerPlayerIdentifier> implements PokerPlayerDao {

	private static final String COLUMN_FAMILY = "poker_player";

	@Inject
	public PokerPlayerDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<PokerPlayerBean> beanProvider,
			final PokerPlayerBeanMapper mapper,
			final PokerPlayerIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
