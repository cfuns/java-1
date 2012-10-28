package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class LaptopNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public LaptopNode(final TcpCheckBuilder tcpCheckBuilder, final UrlCheckBuilder urlCheckBuilder) {
		// memcached
		{
			final String name = "TCP-Check on local memcached-server";
			final String hostname = "localhost";
			final int port = 11212;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		// apache
		{
			final String name = "TCP-Check on local apache-server";
			final String hostname = "localhost";
			final int port = 80;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		// squid
		{
			final String name = "TCP-Check on local squid-server";
			final String hostname = "127.0.0.1";
			final int port = 3128;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		// privoxy
		{
			final String name = "TCP-Check on local privoxy-server";
			final String hostname = "127.0.0.1";
			final int port = 8118;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		// mysql
		{
			final String name = "TCP-Check on local mysql-server";
			final String hostname = "localhost";
			final int port = 3306;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		// cassandra
		{
			final String name = "TCP-Check on local cassandra-server";
			final String hostname = "localhost";
			final int port = 9160;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		// activemq
		{
			final String name = "TCP-Check on local activemq-server";
			final String hostname = "localhost";
			final int port = 61616;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		// activemq-admin
		{
			final String name = "TCP-Check on local activemq-admin-gui";
			final String hostname = "localhost";
			final int port = 8161;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(name, hostname, port)));
		}
		// activemq-admin-gui
		{
			final String name = "URL-Check on local activemq-admin-gui";
			final String url = "http://0.0.0.0:8161/admin/queues.jsp";
			final String titleMatch = "localhost : Queues";
			final String contentMatch = ">Queues<";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
		// openfire-admin-gui
		{
			final String name = "URL-Check on local openfire-gui";
			final String url = "http://0.0.0.0:9090/login.jsp";
			final String titleMatch = "Openfire Admin Console";
			final String contentMatch = " Administration Console";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(name, url, titleMatch, contentMatch)));
		}
	}
}
