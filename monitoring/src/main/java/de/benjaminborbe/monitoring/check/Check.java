package de.benjaminborbe.monitoring.check;

public interface Check {

	CheckResult check();

	String getDescription();
}
