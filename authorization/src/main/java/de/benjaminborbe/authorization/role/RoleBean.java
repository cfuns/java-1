package de.benjaminborbe.authorization.role;

import de.benjaminborbe.authorization.api.Role;
import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class RoleBean implements Entity<RoleIdentifier>, Role {

	private static final long serialVersionUID = 5954692477523378479L;

	private RoleIdentifier id;

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

}
