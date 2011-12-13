package de.benjaminborbe.monitoring.check;

import java.io.Serializable;

public interface CheckResult extends Serializable {

	Check check();

	boolean isSuccess();

	String getMessage();

	String getDescription();
}
