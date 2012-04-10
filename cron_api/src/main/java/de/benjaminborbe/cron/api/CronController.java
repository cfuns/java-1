package de.benjaminborbe.cron.api;

public interface CronController {

	boolean isRunning() throws CronControllerException;

	void start() throws CronControllerException;

	void stop() throws CronControllerException;
}
