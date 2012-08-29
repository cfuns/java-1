package de.benjaminborbe.geocaching.api;

import java.io.Serializable;

public class GeoCoordinate implements Serializable {

	private static final long serialVersionUID = -582660886588213843L;

	private final double latitude;

	private final double longitude;

	private final double altitude;

	public GeoCoordinate(final double latitude, final double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = 0d;
	}

	public GeoCoordinate(final double latitude, final double longitude, final double altitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getAltitude() {
		return altitude;
	}

}
