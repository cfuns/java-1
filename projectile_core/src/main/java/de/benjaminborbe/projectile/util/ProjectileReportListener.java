package de.benjaminborbe.projectile.util;

import de.benjaminborbe.projectile.api.ProjectileSlacktimeReportInterval;

public interface ProjectileReportListener {

	public void onReport(final ProjectileSlacktimeReportInterval interval, ProjectileCsvReportToDto report);
}
