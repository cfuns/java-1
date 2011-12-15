package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class LaptopNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public LaptopNode(final TcpCheckBuilder tcpCheckBuilder, final UrlCheckBuilder urlCheckBuilder) {
		// mysql
		{
			final String hostname = "localhost";
			final int port = 3306;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
		// cassandra
		{
			final String hostname = "localhost";
			final int port = 9160;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
		// activemq
		{
			final String hostname = "localhost";
			final int port = 61616;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
		// activemq-admin
		{
			final String hostname = "localhost";
			final int port = 8161;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
		// mysql
		{
			final String hostname = "localhost";
			final int port = 3306;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
		// activemq-admin-gui
		{
			final String url = "http://0.0.0.0:8161/admin/queues.jsp";
			final String titleMatch = "localhost : Queues";
			final String contentMatch = ">Queues<";
			addNode(new HasCheckNodeImpl(urlCheckBuilder.buildCheck(url, titleMatch, contentMatch)));
		}
	}
}
