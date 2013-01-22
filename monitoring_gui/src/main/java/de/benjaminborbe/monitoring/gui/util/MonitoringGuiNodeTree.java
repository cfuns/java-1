package de.benjaminborbe.monitoring.gui.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.benjaminborbe.monitoring.api.MonitoringNode;
import de.benjaminborbe.monitoring.api.MonitoringNodeIdentifier;

public class MonitoringGuiNodeTree {

	private final Collection<MonitoringNode> nodes;

	public MonitoringGuiNodeTree(final Collection<MonitoringNode> nodes) {
		this.nodes = nodes;
	}

	public List<MonitoringNode> getRootNodes() {
		final List<MonitoringNode> result = new ArrayList<MonitoringNode>();
		for (final MonitoringNode node : nodes) {
			if (node.getParentId() == null) {
				result.add(node);
			}
		}
		Collections.sort(result, new MonitoringNodeComparator());
		return result;
	}

	public List<MonitoringNode> getChildNodes(final MonitoringNodeIdentifier parentId) {
		final List<MonitoringNode> result = new ArrayList<MonitoringNode>();
		for (final MonitoringNode node : nodes) {
			if (parentId.equals(node.getParentId())) {
				result.add(node);
			}
		}
		Collections.sort(result, new MonitoringNodeComparator());
		return result;
	}
}
