package de.benjaminborbe.virt.core.dao;

import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;
import de.benjaminborbe.virt.api.VirtNetwork;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;

import java.util.Calendar;

public class VirtNetworkBean extends EntityBase<VirtNetworkIdentifier> implements VirtNetwork, HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private VirtNetworkIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public VirtNetworkIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final VirtNetworkIdentifier id) {
		this.id = id;
	}

}
