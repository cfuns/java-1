package de.benjaminborbe.authorization.dao;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.Calendar;

public class PermissionBean extends EntityBase<PermissionIdentifier> implements HasCreated, HasModified {

	private static final long serialVersionUID = 5725008696995772288L;

	private PermissionIdentifier id;

	private Calendar created;

	private Calendar modified;

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
	public PermissionIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final PermissionIdentifier id) {
		this.id = id;
	}

}
