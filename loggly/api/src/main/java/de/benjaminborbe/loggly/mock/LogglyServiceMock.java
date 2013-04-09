package de.benjaminborbe.loggly.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.loggly.api.LogglyService;

@Singleton
public class LogglyServiceMock implements LogglyService {

	@Inject
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
