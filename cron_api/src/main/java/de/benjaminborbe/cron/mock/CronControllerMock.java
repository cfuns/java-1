package de.benjaminborbe.cron.mock;

import de.benjaminborbe.cron.api.CronController;
import de.benjaminborbe.cron.api.CronControllerException;

public class CronControllerMock implements CronController {

	public CronControllerMock() {
	}

	@Override
	public void start() throws CronControllerException {
	}

	@Override
	public void stop() throws CronControllerException {
	}

	@Override
	public boolean isRunning() throws CronControllerException {
		return false;
	}

}
