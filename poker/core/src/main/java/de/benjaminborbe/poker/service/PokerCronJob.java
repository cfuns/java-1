package de.benjaminborbe.poker.service;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.poker.config.PokerCoreConfig;
import de.benjaminborbe.poker.util.PokerAutoFoldAllGames;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PokerCronJob implements CronJob {

	private final Logger logger;

	private final PokerCoreConfig pokerCoreConfig;

	private final PokerAutoFoldAllGames pokerAutoFolder;

	@Inject
	public PokerCronJob(final Logger logger, final PokerCoreConfig pokerCoreConfig, final PokerAutoFoldAllGames pokerAutoFolder) {
		this.logger = logger;
		this.pokerCoreConfig = pokerCoreConfig;
		this.pokerAutoFolder = pokerAutoFolder;
	}

	@Override
	public void execute() {
		if (pokerCoreConfig.isCronEnabled()) {
			logger.debug("poker cron => started");
			pokerAutoFolder.processAllGames();
			logger.debug("poker cron => finished");
		} else {
			logger.debug("poker cron => skip");
		}
	}

	@Override
	public String getScheduleExpression() {
		return pokerCoreConfig.getScheduleExpression();
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
