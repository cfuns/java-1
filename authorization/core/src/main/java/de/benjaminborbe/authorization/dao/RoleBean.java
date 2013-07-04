package de.benjaminborbe.authorization.dao;

import de.benjaminborbe.authorization.api.Role;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.Calendar;

public class RoleBean extends EntityBase<RoleIdentifier> implements Role, HasCreated, HasModified {

	private static final long serialVersionUID = 5954692477523378479L;

	private RoleIdentifier id;

	private Calendar modified;

	private Calendar created;

	@Override
	public String getName() {
		return getId() != null ? getId().getId() : null;
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
	public RoleIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final RoleIdentifier id) {
		this.id = id;
	}

}
