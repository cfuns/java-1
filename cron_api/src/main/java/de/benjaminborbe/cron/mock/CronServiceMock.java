package de.benjaminborbe.cron.mock;

import java.util.ArrayList;
import java.util.List;

import de.benjaminborbe.cron.api.CronExecutionInfo;
import de.benjaminborbe.cron.api.CronService;

public class CronServiceMock implements CronService {

	@Override
	public List<CronExecutionInfo> getLatestExecutionInfos(final int amount) {
		return new ArrayList<CronExecutionInfo>();
	}

}
