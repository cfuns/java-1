package de.benjaminborbe.dhl.status;

import java.util.Calendar;

import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class DhlBean implements Entity<DhlIdentifier>, Dhl, HasCreated, HasModified {

	private static final long serialVersionUID = 7960293316768233173L;

	private DhlIdentifier id;

	private long trackingNumber;

	private long zip;

	private Calendar created;

	private Calendar modified;

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

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

}
