package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.monitoring.api.MonitoringCheck;
import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

public class NodeCheckerImpl implements NodeChecker {

	private static final int RETRY_LIMIT = 3;

	private final Logger logger;

	private final SilentNodeRegistry silentNodeRegistry;

	@Inject
	public NodeCheckerImpl(final Logger logger, final SilentNodeRegistry silentNodeRegistry) {
		this.logger = logger;
		this.silentNodeRegistry = silentNodeRegistry;
	}

	@Override
	public Collection<MonitoringCheckResult> checkNode(final Node node) {
		logger.trace("checkNode: " + node);
		final Set<MonitoringCheckResult> result = new HashSet<MonitoringCheckResult>();

		// return empty result if precontition fails
		if (node instanceof HasPreconditionCheckNode) {
			final HasPreconditionCheckNode hasPreconditionCheckNode = (HasPreconditionCheckNode) node;
			final MonitoringCheck check = hasPreconditionCheckNode.getPreconditionCheck();
			if (!check.check().isSuccess()) {
				return result;
			}
		}

		// if node is check node add result
		if (node instanceof HasCheckNode) {
			final HasCheckNode hasCheck = (HasCheckNode) node;
			final MonitoringCheck check = hasCheck.getCheck();
			// skip if found in silentNodes
			for (final String nodeName : silentNodeRegistry.getAll()) {
				logger.trace("compare " + check.getName() + " with " + nodeName);
				if (check.getName().equalsIgnoreCase(nodeName)) {
					return result;
				}
			}
			final RetryCheck retryCheck = new RetryCheck(check, RETRY_LIMIT);
			final MonitoringCheckResult checkResult = retryCheck.check();
			result.add(checkResult);
		}

		// call childs
		if (node instanceof HasChildNodes) {
			final HasChildNodes hasChildNodes = (HasChildNodes) node;
			final Collection<Node> childNodes = hasChildNodes.getChildNodes();
			for (final Node childNode : childNodes) {
				result.addAll(checkNode(childNode));
			}
		}

		return result;
	}
}
