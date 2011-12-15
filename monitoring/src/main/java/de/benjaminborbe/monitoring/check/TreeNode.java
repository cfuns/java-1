package de.benjaminborbe.monitoring.check;

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
