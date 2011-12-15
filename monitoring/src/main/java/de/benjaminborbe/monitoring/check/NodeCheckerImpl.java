package de.benjaminborbe.monitoring.check;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.inject.Inject;

public class NodeCheckerImpl implements NodeChecker {

	private static final int RETRY_LIMIT = 3;

	@Inject
	public NodeCheckerImpl() {
	}

	@Override
	public Collection<CheckResult> checkNode(final Node node) {
		final Set<CheckResult> result = new HashSet<CheckResult>();

		boolean success;
		if (node instanceof HasCheckNode) {
			final HasCheckNode hasCheck = (HasCheckNode) node;
			final Check check = hasCheck.getCheck();
			final RetryCheck retryCheck = new RetryCheck(check, RETRY_LIMIT);
			final CheckResult checkResult = retryCheck.check();
			result.add(checkResult);
			success = checkResult.isSuccess();
		}
		else {
			success = true;
		}
		// only call childs if success or no check
		if (success && node instanceof HasChildNodes) {
			final HasChildNodes hasChildNodes = (HasChildNodes) node;
			final Collection<Node> childNodes = hasChildNodes.getChildNodes();
			for (final Node childNode : childNodes) {
				result.addAll(checkNode(childNode));
			}
		}

		return result;
	}

}
