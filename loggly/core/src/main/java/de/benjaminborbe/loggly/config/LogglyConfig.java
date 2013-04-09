package de.benjaminborbe.loggly.config;

public interface LogglyConfig {

	int getBacklog();

	int getMaxThreads();

	String getInputUrl();

}
