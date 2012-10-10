package de.benjaminborbe.monitoring.check.twentyfeet;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.check.HasCheckNodeImpl;
import de.benjaminborbe.monitoring.check.HasChildNodes;
import de.benjaminborbe.monitoring.check.HasChildNodesImpl;
import de.benjaminborbe.monitoring.check.TcpCheckBuilder;
import de.benjaminborbe.monitoring.check.UrlCheckBuilder;
import de.benjaminborbe.monitoring.config.MonitoringConfig;

public class TwentyfeetLiveNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public TwentyfeetLiveNode(
			final Logger logger,
			final TcpCheckBuilder tcpCheckBuilder,
			final UrlCheckBuilder urlCheckBuilder,
			final TwentyfeetQueueNode twentyfeetQueueNode,
			final MonitoringConfig monitoringConfig) {

		addNode(twentyfeetQueueNode);

		// tcp-checks
		{
			final String name = "TCP-Check on www.twentyfeet.com:80";
			final String hostname = "www.twentyfeet.com";
			final int port = 80;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		{
			final String name = "TCP-Check on www.twentyfeet.com:443";
			final String hostname = "www.twentyfeet.com";
			final int port = 443;
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
			final String name = "URL-Check on wiki.twentyfeet.com";
			final String url = "https://wiki.twentyfeet.com/dashboard.action";
			final String titleMatch = "Dashboard - TwentyFeet Wiki";
			final String contentMatch = "Editors' Wiki";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
		{
			final String name = "URL-Check on www.twentyfeet.com/admin/queues.jsp";
			final String url = "https://www.twentyfeet.com/admin/queues.jsp";
			final String titleMatch = "localhost : Queues";
			final String contentMatch = "<h2>Queues</h2>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch, monitoringConfig.getTwentyfeetAdminUsername(),
					monitoringConfig.getTwentyfeetAdminPassword())));
		}
		{
			final String name = "URL-Check on www.twentyfeet.com/app/admin/";
			final String url = "https://www.twentyfeet.com/app/admin/";
			final String titleMatch = null;
			final String contentMatch = "<h1>Admin</h1>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch, monitoringConfig.getTwentyfeetAdminUsername(),
					monitoringConfig.getTwentyfeetAdminPassword())));
		}
	}
}
