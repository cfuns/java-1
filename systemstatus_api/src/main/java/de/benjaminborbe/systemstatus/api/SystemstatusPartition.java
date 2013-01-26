package de.benjaminborbe.systemstatus.api;

public interface SystemstatusPartition {

	String getAbsolutePath();

	long getFreeSpace();

	long getUsableSpace();

	long getTotalSpace();

	long getUsedSpace();

}
