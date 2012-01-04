package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class InternetNode extends TreeNode implements HasChildNodes, HasPreconditionCheckNode {

	@Inject
	public InternetNode(
			final TwentyfeetNode twentyfeetNode,
			final TcpCheckBuilder tcpCheckBuilder,
			final UrlCheckBuilder urlCheckBuilder) {
		super(tcpCheckBuilder.buildCheck("TCP-Check on www.google.de:80", "www.google.de", 80));

		// childs-checks
		// addNode(twentyfeetNode);

		// tcp-checks

		{
			// git.rocketnews.de
			final String name = "TCP-Check on git.rocketnews.de:22";
			final String hostname = "77.244.108.206";
			final int port = 22;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		{
			// www.rocketnews.de
			final String name = "TCP-Check on www.rocketnews.de:22";
			final String hostname = "77.244.108.198";
			final int port = 22;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		{
			// git.rocketnews.de
			final String name = "TCP-Check on git.rocketnews.de:443";
			final String hostname = "77.244.108.206";
			final int port = 443;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		{
			// www.rocketnews.de
			final String name = "TCP-Check on www.rocketnews.de:443";
			final String hostname = "77.244.108.198";
			final int port = 443;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		// url-checks
		{
			final String name = "URL-Check on www.benjamin-borbe.de";
			final String url = "http://www.benjamin-borbe.de/";
			final String titleMatch = "Portrait - Benjamin Borbe";
			final String contentMatch = "<span class=\"photography\">photography</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on www.benjaminborbe.de";
			final String url = "http://www.benjaminborbe.de/";
			final String titleMatch = "Portrait - Benjamin Borbe";
			final String contentMatch = "<span class=\"photography\">photography</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on www.harteslicht.com";
			final String url = "http://www.harteslicht.com/";
			final String titleMatch = "harteslicht.com | Portrait Beauty Shooting in Wiesbaden";
			final String contentMatch = "<div id=\"site-description\">von Benjamin Borbe";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on jira.benjamin-borbe.de";
			final String url = "http://jira.benjamin-borbe.de/";
			final String titleMatch = "System Dashboard - Rocketnews - Jira";
			final String contentMatch = "View your current Dashboard";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
		{
			final String name = "URL-Check on confluence.benjamin-borbe.de";
			final String url = "http://confluence.benjamin-borbe.de/";
			final String titleMatch = "Dashboard - Confluence";
			final String contentMatch = "<span>Dashboard</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
	}
}
