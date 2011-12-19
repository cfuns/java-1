package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class OfficeNode extends TreeNode implements HasChildNodes, HasPreconditionCheckNode {

	@Inject
	public OfficeNode(final TcpCheckBuilder tcpCheckBuilder) {
		super(tcpCheckBuilder.buildCheck("TCP-Check on timetracker.rp.seibert-media.net:443",
				"timetracker.rp.seibert-media.net", 443));

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
	}

}
