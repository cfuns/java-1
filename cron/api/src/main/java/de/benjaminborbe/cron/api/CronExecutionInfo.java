package de.benjaminborbe.cron.api;

import java.util.Calendar;

public class CronExecutionInfo {

	private final String name;

	private final Calendar time;

	public CronExecutionInfo(final String name, final Calendar time) {
		this.name = name;
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public Calendar getTime() {
		return time;
	}

}
