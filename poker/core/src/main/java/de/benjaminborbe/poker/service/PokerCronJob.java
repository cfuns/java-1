package de.benjaminborbe.poker.service;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.poker.config.PokerConfig;
import de.benjaminborbe.poker.util.PokerAutoCaller;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PokerCronJob implements CronJob {

	/* s m h d m dw y */
	private static final String SCHEDULE_EXPRESSION = "*/3 * * * * ?";

	private final Logger logger;

	private final PokerConfig pokerConfig;

	private final PokerAutoCaller pokerAutoCaller;

	@Inject
	public PokerCronJob(final Logger logger, final PokerConfig pokerConfig, final PokerAutoCaller pokerAutoCaller) {
		this.logger = logger;
		this.pokerConfig = pokerConfig;
		this.pokerAutoCaller = pokerAutoCaller;
	}

	@Override
	public void execute() {
		if (pokerConfig.isCronEnabled()) {
			logger.debug("poker cron => started");
			pokerAutoCaller.run();
			logger.debug("poker cron => finished");
		} else {
			logger.debug("poker cron => skip");
		}
	}

	@Override
	public String getScheduleExpression() {
		return SCHEDULE_EXPRESSION;
	}

	@Override
	public boolean disallowConcurrentExecution() {
		return true;
	}

}
