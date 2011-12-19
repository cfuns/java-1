package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class TwentyfeetNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public TwentyfeetNode(final TcpCheckBuilder tcpCheckBuilder, final UrlCheckBuilder urlCheckBuilder) {
		// tcp-checks
		{
			final String name = "TCP-Check on test.twentyfeet.com:22";
			final String hostname = "test.twentyfeet.com";
			final int port = 22;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		// url-checks
		{
			final String name = "URL-Check on www.twentyfeet.com";
			final String url = "https://www.twentyfeet.com/index.xhtml";
			final String titleMatch = "TwentyFeet - Social Media Monitoring &amp; Ego tracking";
			final String contentMatch = "<a id=\"logo_l\" href=\"/\">TwentyFeet Online-Performance-Tracking</a>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on test.twentyfeet.com";
			final String url = "https://test.twentyfeet.com/index.xhtml";
			final String titleMatch = "TwentyFeet - Social Media Monitoring &amp; Ego tracking";
			final String contentMatch = "<a id=\"logo_l\" href=\"/\">TwentyFeet Online-Performance-Tracking</a>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on www.twentyfeet.com/wiki";
			final String url = "https://www.twentyfeet.com/wiki/dashboard.action";
			final String titleMatch = "Dashboard - TwentyFeet Wiki";
			final String contentMatch = "Editors' Wiki";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
	}
}
