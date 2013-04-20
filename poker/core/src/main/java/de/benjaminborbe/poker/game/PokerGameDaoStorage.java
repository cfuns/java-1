package de.benjaminborbe.poker.game;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;
import javax.inject.Singleton;

import de.benjaminborbe.poker.api.PokerGameIdentifier;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.tools.date.CalendarUtil;

@Singleton
public class PokerGameDaoStorage extends DaoStorage<PokerGameBean, PokerGameIdentifier> implements PokerGameDao {

	private static final String COLUMN_FAMILY = "poker_game";

	@Inject
	public PokerGameDaoStorage(
			final Logger logger,
			final StorageService storageService,
			final Provider<PokerGameBean> beanProvider,
			final PokerGameBeanMapper mapper,
			final PokerGameIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

}
