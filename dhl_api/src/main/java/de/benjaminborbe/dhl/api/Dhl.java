package de.benjaminborbe.dhl.api;

public interface Dhl {

	DhlIdentifier getId();

	String getTrackingNumber();

	Long getZip();

}
