package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

@Singleton
public class NodeCheckerCache implements NodeChecker {

	private final NodeChecker nodeChecker;

	private final Map<Node, Collection<MonitoringCheckResult>> lastResults = new HashMap<Node, Collection<MonitoringCheckResult>>();

	@Inject
	public NodeCheckerCache(final NodeCheckerImpl nodeChecker) {
		this.nodeChecker = nodeChecker;
	}

	@Override
	public Collection<MonitoringCheckResult> checkNode(final Node node) {
		final Collection<MonitoringCheckResult> lastResult = nodeChecker.checkNode(node);
		lastResults.put(node, lastResult);
		return lastResult;
	}

	public Collection<MonitoringCheckResult> checkNodeWithCache(final Node node) {
		if (lastResults.containsKey(node)) {
			return lastResults.get(node);
		}
		else {
			return checkNode(node);
		}
	}

}
