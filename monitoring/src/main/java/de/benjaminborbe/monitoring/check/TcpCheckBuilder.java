package de.benjaminborbe.monitoring.check;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class TcpCheckBuilder {

	private TcpCheckBuilder() {

	}

	@Inject
	public static void link(final Logger logger, final CheckRegistry registry) {
		// google-website
		{
			final String hostname = "www.google.de";
			final int port = 80;
			registry.register(new TcpCheck(logger, hostname, port));
		}
		// dev-cassandra
		{
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 9160;
			registry.register(new TcpCheck(logger, hostname, port));
		}
		// mysql
		{
			final String hostname = "localhost";
			final int port = 3306;
			registry.register(new TcpCheck(logger, hostname, port));
		}
		// cassandra
		{
			final String hostname = "localhost";
			final int port = 9160;
			registry.register(new TcpCheck(logger, hostname, port));
		}
		// activemq
		{
			final String hostname = "localhost";
			final int port = 61616;
			registry.register(new TcpCheck(logger, hostname, port));
		}
		// activemq-admin
		{
			final String hostname = "localhost";
			final int port = 8161;
			registry.register(new TcpCheck(logger, hostname, port));
		}

	}
}
