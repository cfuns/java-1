package de.benjaminborbe.cron.service;

import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.cron.api.CronExecutionInfo;
import de.benjaminborbe.cron.api.CronService;
import de.benjaminborbe.cron.util.CronExecutionHistory;

@Singleton
public class CronServiceImpl implements CronService {

	private final CronExecutionHistory cronExecutionHistory;

	private final Logger logger;

	@Inject
	public CronServiceImpl(final Logger logger, final CronExecutionHistory cronExecutionHistory) {
		this.logger = logger;
		this.cronExecutionHistory = cronExecutionHistory;
	}

	@Override
	public List<CronExecutionInfo> getLatestExecutionInfos(final int amount) {
		logger.debug("getLatestExecutionInfos amount = " + amount);
		final List<CronExecutionInfo> result = cronExecutionHistory.getLatestElements(amount);
		logger.debug("getLatestExecutionInfos found " + result.size());
		return result;
	}
}
