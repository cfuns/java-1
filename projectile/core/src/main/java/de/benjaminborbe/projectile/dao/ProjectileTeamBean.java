package de.benjaminborbe.projectile.dao;

import de.benjaminborbe.projectile.api.ProjectileTeam;
import de.benjaminborbe.projectile.api.ProjectileTeamIdentifier;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.Calendar;

public class ProjectileTeamBean extends EntityBase<ProjectileTeamIdentifier> implements HasCreated, ProjectileTeam, HasModified {

	private static final long serialVersionUID = 5362529362425496903L;

	private ProjectileTeamIdentifier id;

	private Calendar created;

	private Calendar modified;

	private String name;

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
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public ProjectileTeamIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final ProjectileTeamIdentifier id) {
		this.id = id;
	}
}
