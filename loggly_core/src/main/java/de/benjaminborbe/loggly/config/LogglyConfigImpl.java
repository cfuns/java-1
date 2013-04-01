package de.benjaminborbe.loggly.config;

public class LogglyConfigImpl implements LogglyConfig {

	@Override
	public int getBacklog() {
		return 5000;
	}

	@Override
	public int getMaxThreads() {
		return 10;
	}

	@Override
	public String getInputUrl() {
		return "https://logs.loggly.com/inputs/7d416044-0d11-4ed8-ac2a-2b46ef0a2cfd";
	}

}
