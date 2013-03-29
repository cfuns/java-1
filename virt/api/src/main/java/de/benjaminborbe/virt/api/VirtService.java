package de.benjaminborbe.virt.api;

public interface VirtService {

	long calc(long value) throws VirtServiceException;

	VirtNetworkIdentifier createVirtNetworkIdentifier(String id) throws VirtServiceException;
}
