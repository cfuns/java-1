package de.benjaminborbe.dhl.api;

public interface Dhl {

	DhlIdentifier getId();

	long getTrackingNumber();

	long getZip();

}
