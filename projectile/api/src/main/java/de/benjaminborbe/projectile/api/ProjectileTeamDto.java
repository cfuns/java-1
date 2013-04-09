package de.benjaminborbe.projectile.api;

public class ProjectileTeamDto implements ProjectileTeam {

	private ProjectileTeamIdentifier id;

	private String name;

	@Override
	public ProjectileTeamIdentifier getId() {
		return id;
	}

	public void setId(final ProjectileTeamIdentifier id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
