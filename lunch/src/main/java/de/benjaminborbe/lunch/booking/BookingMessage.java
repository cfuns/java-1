package de.benjaminborbe.lunch.booking;

import java.util.Calendar;

public class BookingMessage {

	private final Calendar date;

	private final String customerNumber;

	public BookingMessage(final String customerNumber, final Calendar date) {
		this.customerNumber = customerNumber;
		this.date = date;
	}

	public Calendar getDate() {
		return date;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

}
