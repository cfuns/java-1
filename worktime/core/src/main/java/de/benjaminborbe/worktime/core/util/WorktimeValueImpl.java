package de.benjaminborbe.worktime.core.util;

import java.util.Calendar;

public class WorktimeValueImpl implements WorktimeValue {

	private final Calendar calendar;

	private final boolean inOffice;

	public WorktimeValueImpl(final Calendar calendar, final boolean inOffice) {
		this.calendar = calendar;
		this.inOffice = inOffice;
	}

	@Override
	public Calendar getDate() {
		return calendar;
	}

	@Override
	public boolean getInOffice() {
		return inOffice;
	}
}
