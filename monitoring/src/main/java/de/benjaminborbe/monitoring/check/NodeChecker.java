package de.benjaminborbe.monitoring.check;

import java.util.Collection;

import de.benjaminborbe.monitoring.api.CheckResult;

public interface NodeChecker {

	Collection<CheckResult> checkNode(final Node node);

}
