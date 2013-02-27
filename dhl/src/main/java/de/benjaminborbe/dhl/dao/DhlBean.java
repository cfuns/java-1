package de.benjaminborbe.dhl.dao;

import java.util.Calendar;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class DhlBean extends EntityBase<DhlIdentifier> implements Dhl, HasCreated, HasModified {

	private static final long serialVersionUID = 6881926028300001598L;

	private DhlIdentifier id;

	private String trackingNumber;

	private Long zip;

	private Calendar created;

	private Calendar modified;

	private UserIdentifier owner;

	private String status;

	@Override
	public String getTrackingNumber() {
		return trackingNumber;
	}

	@Override
	public Long getZip() {
		return zip;
	}

	public void setTrackingNumber(final String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public void setZip(final Long zip) {
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

	@Override
	public DhlIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final DhlIdentifier id) {
		this.id = id;
	}

	public UserIdentifier getOwner() {
		return owner;
	}

	public void setOwner(final UserIdentifier owner) {
		this.owner = owner;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

}
