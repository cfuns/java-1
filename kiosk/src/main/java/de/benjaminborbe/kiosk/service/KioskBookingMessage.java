package de.benjaminborbe.kiosk.service;

public class KioskBookingMessage {

	private Long customer;

	private Long ean;

	public KioskBookingMessage() {
	}

	public KioskBookingMessage(final Long customer, final Long ean) {
		this.customer = customer;
		this.setEan(ean);
	}

	public Long getCustomer() {
		return customer;
	}

	public void setCustomer(final Long customer) {
		this.customer = customer;
	}

	public Long getEan() {
		return ean;
	}

	public void setEan(final Long ean) {
		this.ean = ean;
	}

}
