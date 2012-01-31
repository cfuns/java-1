package de.benjaminborbe.cron.api;

public interface CronJob {

	/**
	 * s m h d m dw y
	 * 1 * * * * ?";
	 */
	String getScheduleExpression();

	/**
	 * exec the cron-action
	 */
	void execute();
}
