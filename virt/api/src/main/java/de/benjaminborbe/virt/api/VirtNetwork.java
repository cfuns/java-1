package de.benjaminborbe.virt.api;

public interface VirtNetwork {

	String getName();

	VirtNetworkIdentifier getId();

	VirtIpAddress getIp();
}
