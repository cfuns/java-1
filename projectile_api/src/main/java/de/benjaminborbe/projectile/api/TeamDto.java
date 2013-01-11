package de.benjaminborbe.projectile.api;

public class TeamDto implements Team {

	private TeamIdentifier id;

	private String name;

	@Override
	public TeamIdentifier getId() {
		return id;
	}

	public void setId(final TeamIdentifier id) {
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
