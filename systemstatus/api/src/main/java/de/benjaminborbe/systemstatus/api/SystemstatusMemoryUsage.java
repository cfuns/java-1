package de.benjaminborbe.systemstatus.api;

public interface SystemstatusMemoryUsage {

	long getNonHeapMax();

	long getNonHeapUsed();

	long getHeapMax();

	long getHeapUsed();

}
