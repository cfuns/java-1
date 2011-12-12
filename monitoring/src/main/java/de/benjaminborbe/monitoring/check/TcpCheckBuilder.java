package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class TcpCheckBuilder {

	private TcpCheckBuilder() {

	}

	@Inject
	public static void link(final Logger logger, final CheckRegistry registry) {
		{
			final String hostname = "www.google.de";
			final int port = 80;
			registry.register(new TcpCheck(logger, hostname, port));
		}
		{
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 9160;
			registry.register(new TcpCheck(logger, hostname, port));
		}
	}
}
