package de.benjaminborbe.dhl.api;

public interface Dhl {

	DhlIdentifier getId();

	Long getTrackingNumber();

	Long getZip();

}
