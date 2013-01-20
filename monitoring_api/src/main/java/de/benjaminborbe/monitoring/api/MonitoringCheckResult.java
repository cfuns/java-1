package de.benjaminborbe.monitoring.api;

import java.io.Serializable;
import java.net.URL;

public interface MonitoringCheckResult extends Serializable {

	MonitoringCheck getCheck();

	boolean isSuccess();

	String getMessage();

	String getName();

	String getDescription();

	URL getUrl();
}
