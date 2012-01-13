package de.benjaminborbe.tools.util;

import com.google.inject.Inject;

import de.benjaminborbe.tools.date.CalendarUtil;

public class Duration {

	private final long startTime;

	private final CalendarUtil calendarUtil;

	@Inject
	public Duration(final CalendarUtil calendarUtil) {
		this.calendarUtil = calendarUtil;
		startTime = getNowTime();
	}

	protected long getNowTime() {
		return calendarUtil.getTime();
	}

	public long getTime() {
		return getNowTime() - startTime;
	}
}
