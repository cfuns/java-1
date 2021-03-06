package de.benjaminborbe.poker.guice;

import com.google.inject.AbstractModule;
import de.benjaminborbe.poker.api.PokerService;
import de.benjaminborbe.poker.config.PokerCoreConfig;
import de.benjaminborbe.poker.config.PokerCoreConfigImpl;
import de.benjaminborbe.poker.game.PokerGameDao;
import de.benjaminborbe.poker.game.PokerGameDaoStorage;
import de.benjaminborbe.poker.player.PokerPlayerDao;
import de.benjaminborbe.poker.player.PokerPlayerDaoStorage;
import de.benjaminborbe.poker.service.PokerServiceImpl;
import de.benjaminborbe.tools.log.LoggerSlf4Provider;
import org.slf4j.Logger;

import javax.inject.Singleton;

public class PokerModule extends AbstractModule {

	@Override
	protected void configure() {

		bind(PokerCoreConfig.class).to(PokerCoreConfigImpl.class).in(Singleton.class);
		bind(PokerPlayerDao.class).to(PokerPlayerDaoStorage.class).in(Singleton.class);
		bind(PokerGameDao.class).to(PokerGameDaoStorage.class).in(Singleton.class);
		bind(PokerService.class).to(PokerServiceImpl.class).in(Singleton.class);
		bind(Logger.class).toProvider(LoggerSlf4Provider.class).in(Singleton.class);

		requestStaticInjection(PokerValidatorLinker.class);
	}
}
