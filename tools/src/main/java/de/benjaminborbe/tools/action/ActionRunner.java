package de.benjaminborbe.tools.action;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class ActionRunner {

	private final Logger logger;

	@Inject
	public ActionRunner(final Logger logger) {
		this.logger = logger;
	}

	public void run(final Action action) {
		logger.debug("run action");
		try {
			action.execute();
		}
		catch (final Exception e) {
			logger.debug(e.getClass().getName(), e);
			action.onFailure();
		}
		long time = 0;
		final long waitTimeout = action.getWaitTimeout();
		final long retryDelay = action.getRetryDelay();
		while (true) {
			try {
				final boolean result = action.validateExecuteResult();
				if (result) {
					action.onSuccess();
					return;
				}
			}
			catch (final Exception e) {
				logger.debug(e.getClass().getName(), e);
				action.onFailure();
				return;
			}
			if (time >= waitTimeout || retryDelay == 0) {
				action.onFailure();
				return;
			}
			try {
				Thread.sleep(retryDelay);
			}
			catch (final InterruptedException e) {
				return;
			}
			time += retryDelay;
		}
	}
}
