package de.benjaminborbe.dhl.util;

import java.util.Calendar;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.tools.bk.BkUtil;
import de.benjaminborbe.tools.bk.HasBk;

public class DhlStatus implements HasBk {

	private final Calendar calendar;

	private final String place;

	private final String message;

	private final DhlIdentifier dhlIdentifier;

	public DhlStatus(final DhlIdentifier dhlIdentifier, final Calendar calendar, final String place, final String message) {
		this.dhlIdentifier = dhlIdentifier;
		this.calendar = calendar;
		this.place = place;
		this.message = message;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public String getPlace() {
		return place;
	}

	public String getMessage() {
		return message;
	}

	public DhlIdentifier getDhlIdentifier() {
		return dhlIdentifier;
	}

	@Override
	public boolean equals(final Object other) {
		return BkUtil.equals(this, other);
	}

	@Override
	public String getBk() {
		return dhlIdentifier + "-" + calendar.getTimeInMillis() + "-" + place + "-" + message;
	}
}
