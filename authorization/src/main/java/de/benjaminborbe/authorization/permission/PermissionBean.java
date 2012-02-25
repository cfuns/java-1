package de.benjaminborbe.authorization.permission;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class PermissionBean implements Entity<PermissionIdentifier> {

	private static final long serialVersionUID = 5725008696995772288L;

	private PermissionIdentifier id;

	@Override
	public PermissionIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final PermissionIdentifier id) {
		this.id = id;
	}

}
