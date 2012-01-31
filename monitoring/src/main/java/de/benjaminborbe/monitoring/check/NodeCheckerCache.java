package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class NodeCheckerCache implements NodeChecker {

	private final NodeChecker nodeChecker;

	private final Map<Node, Collection<CheckResult>> lastResults = new HashMap<Node, Collection<CheckResult>>();

	@Inject
	public NodeCheckerCache(final NodeCheckerImpl nodeChecker) {
		this.nodeChecker = nodeChecker;
	}

	@Override
	public Collection<CheckResult> checkNode(final Node node) {
		final Collection<CheckResult> lastResult = nodeChecker.checkNode(node);
		lastResults.put(node, lastResult);
		return lastResult;
	}

	public Collection<CheckResult> checkNodeWithCache(final Node node) {
		if (lastResults.containsKey(node)) {
			return lastResults.get(node);
		}
		else {
			return checkNode(node);
		}
	}

}
