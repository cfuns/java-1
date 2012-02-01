package de.benjaminborbe.monitoring.api;


public interface Check {

	String getName();

	String getDescription();

	CheckResult check();

}
