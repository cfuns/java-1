package de.benjaminborbe.cron.api;

import java.util.List;

public interface CronService {

	List<CronExecutionInfo> getLatestExecutionInfos(int amount);
}
