package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class OfficeNode extends TreeNode implements HasChildNodes, HasPreconditionCheckNode {

	@Inject
	public OfficeNode(final TcpCheckBuilder tcpCheckBuilder) {
		super(tcpCheckBuilder.buildCheck("timetracker.rp.seibert-media.net", 443));

		// dev-cassandra
		{
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 9160;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
		// activemq
		{
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 61616;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
		// activemq-admin
		{
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 8161;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
	}

}
