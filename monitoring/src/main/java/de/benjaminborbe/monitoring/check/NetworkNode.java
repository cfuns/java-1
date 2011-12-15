package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;

public class NetworkNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public NetworkNode(final InternetNode internetNode, final OfficeNode officeNode) {
		addNode(internetNode);
		addNode(officeNode);
	}
}
