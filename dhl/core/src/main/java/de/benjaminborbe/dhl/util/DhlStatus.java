package de.benjaminborbe.dhl.util;

import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.tools.bk.BkUtil;
import de.benjaminborbe.tools.bk.HasBk;

import java.util.Calendar;

public class DhlStatus implements HasBk {

	private final Calendar calendar;

	private final String place;

	private final String message;

	private final Dhl dhl;

	public DhlStatus(final Dhl dhl, final Calendar calendar, final String place, final String message) {
		this.dhl = dhl;
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

	public Dhl getDhl() {
		return dhl;
	}

	@Override
	public boolean equals(final Object other) {
		return BkUtil.equals(this, other);
	}

	@Override
	public String getBk() {
		return dhl.getTrackingNumber() + "-" + calendar.getTimeInMillis() + "-" + place + "-" + message;
	}

	@Override
	public int hashCode() {
		final String bk = getBk();
		return bk != null ? bk.hashCode() : super.hashCode();
	}

	@Override
	public String toString() {
		return getBk();
	}

}
