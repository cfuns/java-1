package de.benjaminborbe.monitoring.check;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RootNode extends HasChildNodesImpl implements HasChildNodes {

	@Inject
	public RootNode(final NetworkNode networkNode, final LaptopNode laptopNode) {
		addNode(networkNode);
		addNode(laptopNode);
	}
}
