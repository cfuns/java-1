package de.benjaminborbe.monitoring.check;

import de.benjaminborbe.monitoring.api.MonitoringCheck;

public interface HasCheckNode extends Node {

	MonitoringCheck getCheck();
}
