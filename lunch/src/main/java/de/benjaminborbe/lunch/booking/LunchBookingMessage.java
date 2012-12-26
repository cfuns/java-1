package de.benjaminborbe.lunch.booking;

import java.util.Calendar;

public class LunchBookingMessage {

	private Calendar date;

	private String customerNumber;

	public LunchBookingMessage() {
	}

	public LunchBookingMessage(final String customerNumber, final Calendar date) {
		this.customerNumber = customerNumber;
		this.date = date;
	}

	public Calendar getDate() {
		return date;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setDate(final Calendar date) {
		this.date = date;
	}

	public void setCustomerNumber(final String customerNumber) {
		this.customerNumber = customerNumber;
	}

}
