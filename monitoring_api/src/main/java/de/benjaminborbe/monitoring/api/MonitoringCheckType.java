package de.benjaminborbe.monitoring.api;

public enum MonitoringCheckType {

	NOP("nop"),
	TCP("tcp"),
	URL("url");

	private final String title;

	private MonitoringCheckType(final String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
