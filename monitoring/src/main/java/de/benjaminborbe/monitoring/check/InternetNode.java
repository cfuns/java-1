package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.check.twentyfeet.TwentyfeetLiveNode;
import de.benjaminborbe.monitoring.check.twentyfeet.TwentyfeetTestNode;

public class InternetNode extends TreeNode implements HasChildNodes, HasPreconditionCheckNode {

	private static final boolean OPENFIRE = true;

	@Inject
	public InternetNode(
			final RisikoNode risikoNode,
			final TwentyfeetLiveNode twentyfeetLiveNode,
			final TwentyfeetTestNode twentyfeetTestNode,
			final TcpCheckBuilder tcpCheckBuilder,
			final UrlCheckBuilder urlCheckBuilder) {
		super(tcpCheckBuilder.buildCheck("TCP-Check on www.google.de:80", "www.google.de", 80));

		// childs-checks
		// addNode(risikoNode);
		// addNode(twentyfeetLiveNode);
		// addNode(twentyfeetTestNode);

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

		{
			final String name = "TCP-Check on iredmail.mailfolder.org:465";
			final String hostname = "iredmail.mailfolder.org";
			final int port = 465;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		{
			final String name = "TCP-Check on iredmail.mailfolder.org:993";
			final String hostname = "iredmail.mailfolder.org";
			final int port = 993;
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
			final String name = "URL-Check on a.benjamin-borbe.de";
			final String url = "http://a.benjamin-borbe.de/";
			final String titleMatch = "Portrait - Benjamin Borbe";
			final String contentMatch = "<span class=\"photography\">photography</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on b.benjamin-borbe.de";
			final String url = "http://b.benjamin-borbe.de/";
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
			final String name = "URL-Check on confluence.benjamin-borbe.de";
			final String url = "http://confluence.benjamin-borbe.de/";
			final String titleMatch = "Dashboard - Confluence";
			final String contentMatch = "<span>Dashboard</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on nexus.benjamin-borbe.de";
			final String url = "http://nexus.benjamin-borbe.de/nexus/index.html";
			final String titleMatch = "Sonatype Nexus";
			final String contentMatch = "<p>Welcome to the ";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on jenkins.benjamin-borbe.de";
			final String url = "http://jenkins.benjamin-borbe.de/login";
			final String titleMatch = "Jenkins";
			final String contentMatch = "<a href=\"/\">Jenkins</a>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on cacti.benjamin-borbe.de";
			final String url = "http://cacti.benjamin-borbe.de/cacti/";
			final String titleMatch = "Login to Cacti";
			final String contentMatch = "Please enter your Cacti user name and password below:";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		if (OPENFIRE) {
			final String name = "URL-Check on openfire.benjamin-borbe.de";
			final String url = "http://openfire.benjamin-borbe.de/login.jsp";
			final String titleMatch = "Openfire Admin Console";
			final String contentMatch = " Administration Console";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
		if (OPENFIRE) {
			final String name = "TCP-Check on openfire.benjamin-borbe.de";
			final String hostname = "openfire.benjamin-borbe.de";
			final int port = 5222;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
	}
}
