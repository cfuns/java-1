package de.benjaminborbe.geocaching.api;

import java.io.Serializable;

public class GeoCoordinate implements Serializable {

	private static final long serialVersionUID = -582660886588213843L;

	private final double latitude;

	private final double longitude;

	public GeoCoordinate(final double latitude, final double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

}
