package de.benjaminborbe.loggly.mock;

import de.benjaminborbe.loggly.api.LogglyService;

public class LogglyServiceMock implements LogglyService {

	public LogglyServiceMock() {
	}

	@Override
	public void trace(final String message) {
	}

	@Override
	public void debug(final String message) {
	}

	@Override
	public void info(final String message) {
	}

	@Override
	public void warn(final String message) {
	}

	@Override
	public void error(final String message) {
	}

	@Override
	public void fatal(final String message) {
	}
}
