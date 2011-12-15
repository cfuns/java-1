package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class InternetNode extends TreeNode implements HasChildNodes, HasPreconditionCheckNode {

	@Inject
	public InternetNode(final TcpCheckBuilder tcpCheckBuilder, final UrlCheckBuilder urlCheckBuilder) {
		super(tcpCheckBuilder.buildCheck("www.google.de", 80));

		// childs-checks

		{
			final String url = "https://www.twentyfeet.com/index.xhtml";
			final String titleMatch = "TwentyFeet - Social Media Monitoring &amp; Ego tracking";
			final String contentMatch = "<a id=\"logo_l\" href=\"/\">TwentyFeet Online-Performance-Tracking</a>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(url, titleMatch, contentMatch)));
		}

		{
			final String url = "https://test.twentyfeet.com/index.xhtml";
			final String titleMatch = "TwentyFeet - Social Media Monitoring &amp; Ego tracking";
			final String contentMatch = "<a id=\"logo_l\" href=\"/\">TwentyFeet Online-Performance-Tracking</a>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(url, titleMatch, contentMatch)));
		}

		{
			final String url = "http://www.benjamin-borbe.de/";
			final String titleMatch = "Portrait - Benjamin Borbe";
			final String contentMatch = "<span class=\"photography\">photography</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(url, titleMatch, contentMatch)));
		}

		{
			final String url = "http://www.benjaminborbe.de/";
			final String titleMatch = "Portrait - Benjamin Borbe";
			final String contentMatch = "<span class=\"photography\">photography</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(url, titleMatch, contentMatch)));
		}

		{
			final String url = "http://www.harteslicht.com/";
			final String titleMatch = "harteslicht.com | Portrait Beauty Shooting in Wiesbaden";
			final String contentMatch = "<div id=\"site-description\">von Benjamin Borbe";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(url, titleMatch, contentMatch)));
		}

		{
			final String url = "http://confluence.rocketnews.de/";
			final String titleMatch = "Dashboard - Confluence";
			final String contentMatch = "<span>Dashboard</span>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(url, titleMatch, contentMatch)));
		}
	}

}
