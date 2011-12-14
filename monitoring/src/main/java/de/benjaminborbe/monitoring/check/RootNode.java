package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class RootNode implements HasChildNodes {

	private final Set<CheckNode> nodes = new HashSet<CheckNode>();

	@Inject
	public RootNode() {

	}

	public void addNode(final CheckNode node) {
		nodes.add(node);
	}

	@Override
	public Collection<CheckNode> getChildNodes() {
		// TODO Auto-generated method stub
		return null;
	}

}
