package de.benjaminborbe.projectile.gui.util;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReport;
import de.benjaminborbe.tools.util.ComparatorBase;

public class ProjectileSlacktimeReportComparator extends ComparatorBase<ProjectileSlacktimeReport, String> {

	@Override
	public String getValue(final ProjectileSlacktimeReport o) {
		return o.getName() != null ? o.getName().toLowerCase() : null;
	}

}
