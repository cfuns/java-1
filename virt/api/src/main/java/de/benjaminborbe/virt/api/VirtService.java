package de.benjaminborbe.virt.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface VirtService {

	VirtNetworkIdentifier createNetworkIdentifier(String id) throws VirtServiceException;

	VirtNetworkIdentifier createNetwork(
		SessionIdentifier sessionIdentifier,
		VirtNetwork network
	) throws VirtServiceException, PermissionDeniedException, ValidationException;

	VirtMachineIdentifier createMachineIdentifier(String id) throws VirtServiceException;

	VirtMachineIdentifier createMachine(SessionIdentifier sessionIdentifier, VirtMachine machine) throws VirtServiceException, PermissionDeniedException;

	VirtVirtualMachineIdentifier createVirtualMachineIdentifier(String id) throws VirtServiceException;

	VirtVirtualMachineIdentifier createVirtualMachine(
		SessionIdentifier sessionIdentifier,
		VirtMachine machine
	) throws VirtServiceException, PermissionDeniedException;

	VirtNetwork getNetwork(SessionIdentifier sessionIdentifier, VirtNetworkIdentifier networkIdentifier) throws VirtServiceException, PermissionDeniedException;
}
