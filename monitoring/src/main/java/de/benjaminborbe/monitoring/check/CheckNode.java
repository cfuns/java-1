package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

public class CheckNode implements HasParentNode, HasChildNodes {

	private CheckNode parentNode;

	private final Set<CheckNode> childNodes = new HashSet<CheckNode>();

	@Inject
	public CheckNode() {
	}

	@Override
	public Collection<CheckNode> getChildNodes() {
		return childNodes;
	}

	public void addChildNode(final CheckNode node) {
		childNodes.add(node);
	}

	@Override
	public CheckNode getParent() {
		return parentNode;
	}

	public void setParentNode(final CheckNode node) {
		this.parentNode = node;
	}
}
