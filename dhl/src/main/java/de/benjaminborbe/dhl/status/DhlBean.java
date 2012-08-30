package de.benjaminborbe.dhl.status;

import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class DhlBean implements Entity<DhlIdentifier>, Dhl {

	private static final long serialVersionUID = 7960293316768233173L;

	private DhlIdentifier id;

	private long trackingNumber;

	private long zip;

	@Override
	public DhlIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final DhlIdentifier id) {
		this.id = id;
	}

	@Override
	public long getTrackingNumber() {
		return trackingNumber;
	}

	@Override
	public long getZip() {
		return zip;
	}

	public void setTrackingNumber(final long trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public void setZip(final long zip) {
		this.zip = zip;
	}

}
