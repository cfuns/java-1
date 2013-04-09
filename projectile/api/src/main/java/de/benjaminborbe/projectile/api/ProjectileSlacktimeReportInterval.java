package de.benjaminborbe.projectile.api;

public enum ProjectileSlacktimeReportInterval {
	WEEK("week"),
	MONTH("month"),
	YEAR("year");

	private final String title;

	ProjectileSlacktimeReportInterval(final String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
