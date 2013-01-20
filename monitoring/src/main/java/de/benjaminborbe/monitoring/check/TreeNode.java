package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.MonitoringCheck;

public class TreeNode extends HasChildNodesImpl implements HasChildNodes, HasPreconditionCheckNode {

	private final MonitoringCheck preconditionCheck;

	public TreeNode(final MonitoringCheck preconditionCheck) {
		this.preconditionCheck = preconditionCheck;
	}

	@Override
	public MonitoringCheck getPreconditionCheck() {
		return preconditionCheck;
	}

}
