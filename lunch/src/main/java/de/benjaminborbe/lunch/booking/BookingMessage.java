package de.benjaminborbe.lunch.booking;

import java.util.Calendar;

public class BookingMessage {

	private final Calendar date;

	private final String user;

	public BookingMessage(final String user, final Calendar date) {
		this.user = user;
		this.date = date;
	}

	public Calendar getDate() {
		return date;
	}

	public String getUser() {
		return user;
	}

}
