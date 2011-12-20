package de.benjaminborbe.worktime.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.worktime.api.Workday;
import de.benjaminborbe.worktime.api.WorktimeService;
import de.benjaminborbe.worktime.util.WorkdayImpl;

@Singleton
public class WorktimeServiceImpl implements WorktimeService {

	private final Logger logger;

	@Inject
	public WorktimeServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public List<Workday> getTimes(final int days) {
		logger.debug("get times for " + days + " days");
		final List<Workday> result = new ArrayList<Workday>();
		result.add(new WorkdayImpl(Calendar.getInstance(), Calendar.getInstance()));
		return result;
	}

}
