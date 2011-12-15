package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class NodeCheckerImpl implements NodeChecker {

	private static final int RETRY_LIMIT = 3;

	private final Logger logger;

	@Inject
	public NodeCheckerImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public Collection<CheckResult> checkNode(final Node node) {
		logger.debug("checkNode: " + node);
		final Set<CheckResult> result = new HashSet<CheckResult>();

		// return empty result if precontition fails
		if (node instanceof HasPreconditionCheckNode) {
			final HasPreconditionCheckNode hasPreconditionCheckNode = (HasPreconditionCheckNode) node;
			final Check check = hasPreconditionCheckNode.getPreconditionCheck();
			if (!check.check().isSuccess()) {
				return result;
			}
		}

		// if node is check node add result
		if (node instanceof HasCheckNode) {
			final HasCheckNode hasCheck = (HasCheckNode) node;
			final Check check = hasCheck.getCheck();
			final RetryCheck retryCheck = new RetryCheck(check, RETRY_LIMIT);
			final CheckResult checkResult = retryCheck.check();
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
