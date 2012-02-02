package de.benjaminborbe.monitoring.api;

import java.io.Serializable;
import java.net.URL;

public interface CheckResult extends Serializable {

	Check getCheck();

	boolean isSuccess();

	String getMessage();

	String getName();

	String getDescription();

	URL getUrl();
}
