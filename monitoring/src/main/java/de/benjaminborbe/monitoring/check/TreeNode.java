package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.Check;

public class TreeNode extends HasChildNodesImpl implements HasChildNodes, HasPreconditionCheckNode {

	private final Check preconditionCheck;

	public TreeNode(final Check preconditionCheck) {
		this.preconditionCheck = preconditionCheck;
	}

	@Override
	public Check getPreconditionCheck() {
		return preconditionCheck;
	}

}
