package de.benjaminborbe.worktime.core.util;

import de.benjaminborbe.worktime.api.Workday;

import java.util.Calendar;

public class WorkdayImpl implements Workday {

	private final Calendar start;

	private final Calendar end;

	private final Calendar date;

	public WorkdayImpl(final Calendar date, final Calendar start, final Calendar end) {
		this.date = date;
		this.start = start;
		this.end = end;
	}

	@Override
	public Calendar getStart() {
		return start;
	}

	@Override
	public Calendar getEnd() {
		return end;
	}

	@Override
	public Calendar getDate() {
		return date;
	}

}
