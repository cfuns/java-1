package de.benjaminborbe.lunch.kioskconnector;

import de.benjaminborbe.lunch.api.LunchUser;

public class KioskUserBean implements LunchUser {

	private String prename;

	private String surname;

	private String customerNumber;

	@Override
	public String getPrename() {
		return prename;
	}

	@Override
	public String getSurname() {
		return surname;
	}

	@Override
	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setPrename(final String prename) {
		this.prename = prename;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	public void setCustomerNumber(final String customerNumber) {
		this.customerNumber = customerNumber;
	}

	@Override
	public String getName() {
		if (prename != null && surname != null) {
			return prename + " " + surname;
		}
		if (prename != null) {
			return prename;
		}
		if (surname != null) {
			return surname;
		}
		return null;
	}

}
