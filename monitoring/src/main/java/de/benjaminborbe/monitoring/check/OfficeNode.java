package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class OfficeNode extends TreeNode implements HasChildNodes, HasPreconditionCheckNode {

	@Inject
	public OfficeNode(final TcpCheckBuilder tcpCheckBuilder, final UrlCheckBuilder urlCheckBuilder, final HudsonCheckBuilder hudsonCheckBuilder) {
		super(tcpCheckBuilder.buildCheck("TCP-Check on timetracker.rp.seibert-media.net:443", "timetracker.rp.seibert-media.net", 443));

		// tcp-checks
		{
			final String name = "TCP-Check on devel cassandra-server";
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 9160;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		{
			final String name = "TCP-Check on devel activemq-server";
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 61616;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		{
			final String name = "TCP-Check on devel activemq-admin-gui";
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 8161;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		// url-checks
		{
			final String name = "URL-Check on projectile.rp.seibert-media.net";
			final String url = "https://projectile.rp.seibert-media.net/projectile/start";
			final String titleMatch = "//SEIBERT/MEDIA GmbH";
			final String contentMatch = "Bitte melden Sie sich an";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
		{
			final String name = "URL-Check on timetracker.rp.seibert-media.net";
			final String url = "https://timetracker.rp.seibert-media.net/timetracker/authentication/login";
			final String titleMatch = "//SEIBERT/MEDIA TimeTracker";
			final String contentMatch = "<span class=\"app_name\">//SEIBERT/MEDIA TimeTracker</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		// hudson checks
		{
			final String name = "Hudson-Check on TwentyFeet-UnitTests";
			final String url = "https://hudson.rp.seibert-media.net/";
			final String job = "20ft UnitTests";
			addNode(new HasCheckNodeImpl(hudsonCheckBuilder.buildCheck(name, url, job)));
		}
		{
			final String name = "Hudson-Check on Twentyfeet-IntegrationTest";
			final String url = "https://hudson.rp.seibert-media.net/";
			final String job = "20ft IntegrationTest";
			addNode(new HasCheckNodeImpl(hudsonCheckBuilder.buildCheck(name, url, job)));
		}
	}
}
