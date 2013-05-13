package de.benjaminborbe.selenium.api.action;

public class SeleniumActionConfigurationSleep implements SeleniumActionConfiguration {

	private final String message;

	private final long duration;

	public long getDuration() {
		return duration;
	}

	public SeleniumActionConfigurationSleep(final String message, final long duration) {
		this.message = message;
		this.duration = duration;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
