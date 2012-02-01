package de.benjaminborbe.cron.api;

public interface CronController {

	void start() throws CronControllerException;

	void stop() throws CronControllerException;
}
