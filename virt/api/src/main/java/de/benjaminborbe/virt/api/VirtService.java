package de.benjaminborbe.virt.api;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface VirtService {

	long calc(long value) throws VirtServiceException;

	VirtNetworkIdentifier createVirtNetworkIdentifier(String id) throws VirtServiceException;

	VirtualMachineIdentifier createVirtualMachine(SessionIdentifier sessionIdentifier, VirtMachineDto virtMachine);
}
