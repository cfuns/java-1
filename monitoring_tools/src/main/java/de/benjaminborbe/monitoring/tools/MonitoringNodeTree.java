package de.benjaminborbe.monitoring.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;

public class MonitoringNodeTree<N extends MonitoringNode> {

	private final Collection<N> nodes;

	public MonitoringNodeTree(final Collection<N> nodes) {
		this.nodes = nodes;
	}

	public List<N> getRootNodes() {
		final List<N> result = new ArrayList<N>();
		for (final N node : nodes) {
			if (node.getParentId() == null) {
				result.add(node);
			}
		}
		Collections.sort(result, new MonitoringNodeComparator<N>());
		return result;
	}

	public List<N> getChildNodes(final MonitoringNodeIdentifier parentId) {
		final List<N> result = new ArrayList<N>();
		for (final N node : nodes) {
			if (parentId.equals(node.getParentId())) {
				result.add(node);
			}
		}
		Collections.sort(result, new MonitoringNodeComparator<N>());
		return result;
	}
}
