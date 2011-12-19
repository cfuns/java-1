package de.benjaminborbe.monitoring.check;

public interface Check {

	String getName();

	String getDescription();

	CheckResult check();

}
