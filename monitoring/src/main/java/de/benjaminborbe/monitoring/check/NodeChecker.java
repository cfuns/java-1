package de.benjaminborbe.monitoring.check;

import java.util.Collection;

public interface NodeChecker {

	Collection<CheckResult> checkNode(final Node node);

}
