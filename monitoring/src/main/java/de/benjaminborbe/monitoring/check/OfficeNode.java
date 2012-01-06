package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class OfficeNode extends TreeNode implements HasChildNodes, HasPreconditionCheckNode {

	@Inject
	public OfficeNode(final TcpCheckBuilder tcpCheckBuilder, final UrlCheckBuilder urlCheckBuilder) {
		super(tcpCheckBuilder.buildCheck("TCP-Check on timetracker.rp.seibert-media.net:443", "timetracker.rp.seibert-media.net", 443));

		// dev-cassandra
		{
			final String name = "TCP-Check on devel cassandra-server";
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 9160;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		// activemq
		{
			final String name = "TCP-Check on devel activemq-server";
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 61616;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		// activemq-admin
		{
			final String name = "TCP-Check on devel activemq-admin-gui";
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 8161;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		// projectile
		{
			final String name = "URL-Check on projectile.rp.seibert-media.net";
			final String url = "https://projectile.rp.seibert-media.net/projectile/start";
			final String titleMatch = "//SEIBERT/MEDIA GmbH";
			final String contentMatch = "Bitte melden Sie sich an";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		// timetracker
		{
			final String name = "URL-Check on timetracker.rp.seibert-media.net";
			final String url = "https://timetracker.rp.seibert-media.net/timetracker/authentication/login";
			final String titleMatch = "//SEIBERT/MEDIA TimeTracker";
			final String contentMatch = "<span class=\"app_name\">//SEIBERT/MEDIA TimeTracker</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
	}
}
