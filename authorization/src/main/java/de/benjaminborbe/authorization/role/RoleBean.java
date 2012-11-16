package de.benjaminborbe.authorization.role;

import java.util.Calendar;

import de.benjaminborbe.authorization.api.Role;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class RoleBean implements Entity<RoleIdentifier>, Role, HasCreated, HasModified {

	private static final long serialVersionUID = 5954692477523378479L;

	private RoleIdentifier id;

	private Calendar modified;

	private Calendar created;

	@Override
	public RoleIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final RoleIdentifier id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return id != null ? id.getId() : null;
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
