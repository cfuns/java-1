package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TcpCheckBuilder {

	private final Logger logger;

	@Inject
	public TcpCheckBuilder(final Logger logger) {
		this.logger = logger;
	}

	public Check buildCheck(final String hostname, final int port) {
		return new TcpCheck(logger, hostname, port);
	}

}
