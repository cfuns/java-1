package de.benjaminborbe.cron.util;

import de.benjaminborbe.cron.api.CronJob;

public interface CronJobRegistry {

	void unregister(CronJob cronJob);

	void register(CronJob cronJob);

	CronJob getByName(String name);

}
