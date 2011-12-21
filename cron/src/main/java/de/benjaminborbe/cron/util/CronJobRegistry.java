package de.benjaminborbe.cron.util;

import de.benjaminborbe.cron.api.CronJob;
import de.benjaminborbe.tools.util.Registry;

public interface CronJobRegistry extends Registry<CronJob> {

	CronJob getByName(String name);

}
