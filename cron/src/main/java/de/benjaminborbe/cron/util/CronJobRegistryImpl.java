package de.benjaminborbe.cron.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronJob;

@Singleton
public class CronJobRegistryImpl implements CronJobRegistry {

	private final Map<String, CronJob> data = new HashMap<String, CronJob>();

	@Override
	public void remove(final CronJob cronJob) {
		data.remove(getName(cronJob));
	}

	protected String getName(final CronJob cronJob) {
		return cronJob.getClass().getName();
	}

	@Override
	public void add(final CronJob cronJob) {
		data.put(getName(cronJob), cronJob);
	}

	@Override
	public CronJob getByName(final String name) {
		return data.get(name);
	}

	@Override
	public Collection<CronJob> getAll() {
		return data.values();
	}
}
