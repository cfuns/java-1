package de.benjaminborbe.loggly.service;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.loggly.api.LogglyService;
import de.benjaminborbe.loggly.util.LogglyConnector;

@Singleton
public class LogglyServiceImpl implements LogglyService {

	private final Provider<LogglyConnector> logglyConnectorProvider;

	@Inject
	public LogglyServiceImpl(final Provider<LogglyConnector> logglyConnectorProvider) {
		this.logglyConnectorProvider = logglyConnectorProvider;
	}

	@Override
	public void trace(final String message) {
		logglyConnectorProvider.get().trace(message);
	}

	@Override
	public void debug(final String message) {
		logglyConnectorProvider.get().debug(message);
	}

	@Override
	public void info(final String message) {
		logglyConnectorProvider.get().info(message);
	}

	@Override
	public void warn(final String message) {
		logglyConnectorProvider.get().warn(message);
	}

	@Override
	public void error(final String message) {
		logglyConnectorProvider.get().error(message);
	}

	@Override
	public void fatal(final String message) {
		logglyConnectorProvider.get().fatal(message);
	}

}
