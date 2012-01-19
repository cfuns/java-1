package de.benjaminborbe.performance.util;

public class PerformanceEntry {

	private final String url;

	private final long duration;

	public PerformanceEntry(final String url, final long duration) {
		this.url = url;
		this.duration = duration;
	}

	public String getUrl() {
		return url;
	}

	public long getDuration() {
		return duration;
	}

}
