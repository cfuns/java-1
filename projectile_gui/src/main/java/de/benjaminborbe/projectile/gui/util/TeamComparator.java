package de.benjaminborbe.projectile.gui.util;

import de.benjaminborbe.projectile.api.Team;
import de.benjaminborbe.tools.util.ComparatorBase;

public class TeamComparator extends ComparatorBase<Team, String> {

	@Override
	public String getValue(final Team o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
