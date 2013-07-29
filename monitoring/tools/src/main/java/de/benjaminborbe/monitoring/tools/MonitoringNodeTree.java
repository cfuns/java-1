package de.benjaminborbe.monitoring.tools;

import de.benjaminborbe.monitoring.api.MonitoringHasParentId;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MonitoringNodeTree<N extends MonitoringHasParentId> {

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
		return result;
	}

	public List<N> getChildNodes(final MonitoringNodeIdentifier parentId) {
		final List<N> result = new ArrayList<N>();
		for (final N node : nodes) {
			if (parentId.equals(node.getParentId())) {
				result.add(node);
			}
		}
		return result;
	}
}
