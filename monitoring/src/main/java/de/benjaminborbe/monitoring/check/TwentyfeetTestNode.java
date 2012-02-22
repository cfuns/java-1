package de.benjaminborbe.monitoring.check;

import javax.naming.NamingException;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.jndi.JndiContext;

public class TwentyfeetTestNode extends TreeNode implements HasChildNodes, HasPreconditionCheckNode {

	@Inject
	public TwentyfeetTestNode(final Logger logger, final TcpCheckBuilder tcpCheckBuilder, final UrlCheckBuilder urlCheckBuilder, final JndiContext jndiContext) {
		super(tcpCheckBuilder.buildCheck("TCP-Check on test.twentyfeet.com:80", "test.twentyfeet.com", 80));

		// tcp-checks
		{
			final String name = "TCP-Check on test.twentyfeet.com:80";
			final String hostname = "test.twentyfeet.com";
			final int port = 80;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		{
			final String name = "TCP-Check on test.twentyfeet.com:443";
			final String hostname = "test.twentyfeet.com";
			final int port = 443;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		{
			final String name = "TCP-Check on test1.twentyfeet.com:22";
			final String hostname = "test1.twentyfeet.com";
			final int port = 22;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		{
			final String name = "TCP-Check on test1.twentyfeet.com:80";
			final String hostname = "test1.twentyfeet.com";
			final int port = 80;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		{
			final String name = "TCP-Check on test1.twentyfeet.com:443";
			final String hostname = "test1.twentyfeet.com";
			final int port = 443;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}

		// url-checks
		{
			final String name = "URL-Check on test.twentyfeet.com";
			final String url = "https://test.twentyfeet.com/index.xhtml";
			final String titleMatch = "TwentyFeet - Social Media Monitoring &amp; Ego tracking";
			final String contentMatch = "<a id=\"logo_l\" href=\"/\">TwentyFeet Online-Performance-Tracking</a>";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}

		{
			final String name = "URL-Check on test.twentyfeet.com/admin/queues.jsp";
			final String url = "https://test.twentyfeet.com/admin/queues.jsp";
			final String titleMatch = "localhost : Queues";
			final String contentMatch = "<h2>Queues</h2>";
			try {
				addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch, (String) jndiContext.lookup("twentyfeet_admin_username"),
						(String) jndiContext.lookup("twentyfeet_admin_password"))));
			}
			catch (final NamingException e) {
				logger.info("skip add node: " + name);
			}
		}
		{
			final String name = "URL-Check on test.twentyfeet.com/app/admin/";
			final String url = "https://test.twentyfeet.com/app/admin/";
			final String titleMatch = null;
			final String contentMatch = "<h1>Admin</h1>";
			try {
				addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch, (String) jndiContext.lookup("twentyfeet_admin_username"),
						(String) jndiContext.lookup("twentyfeet_admin_password"))));
			}
			catch (final NamingException e) {
				logger.info("skip add node: " + name);
			}
		}
	}
}
