package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class OfficeNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public OfficeNode(final TcpCheckBuilder tcpCheckBuilder) {
		// dev-cassandra
		{
			final String hostname = "dev.rp.seibert-media.net";
			final int port = 9160;
			addNode(new HasCheckNodeImpl(tcpCheckBuilder.buildCheck(hostname, port)));
		}
	}

}
