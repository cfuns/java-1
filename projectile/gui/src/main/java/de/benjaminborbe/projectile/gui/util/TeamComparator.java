package de.benjaminborbe.projectile.gui.util;

import de.benjaminborbe.projectile.api.ProjectileTeam;
import de.benjaminborbe.tools.util.ComparatorBase;

public class TeamComparator extends ComparatorBase<ProjectileTeam, String> {

	@Override
	public String getValue(final ProjectileTeam o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
