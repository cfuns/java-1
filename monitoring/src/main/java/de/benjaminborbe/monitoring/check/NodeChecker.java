package de.benjaminborbe.monitoring.check;

import java.util.Collection;

import de.benjaminborbe.monitoring.api.MonitoringCheckResult;

public interface NodeChecker {

	Collection<MonitoringCheckResult> checkNode(final Node node);

}
