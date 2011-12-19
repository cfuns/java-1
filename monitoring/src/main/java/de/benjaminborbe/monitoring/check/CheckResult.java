package de.benjaminborbe.monitoring.check;

import java.io.Serializable;

public interface CheckResult extends Serializable {

	Check getCheck();

	boolean isSuccess();

	String getMessage();

	String getName();

	String getDescription();
}
