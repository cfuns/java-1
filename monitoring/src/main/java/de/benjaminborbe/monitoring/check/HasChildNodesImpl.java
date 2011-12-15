package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class HasChildNodesImpl implements HasChildNodes {

	private final Set<Node> nodes = new HashSet<Node>();

	protected void addNode(final Node node) {
		nodes.add(node);
	}

	@Override
	public Collection<Node> getChildNodes() {
		return nodes;
	}

}
