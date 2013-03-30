package de.benjaminborbe.virt.api;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;

public interface VirtService {

	long calc(long value) throws VirtServiceException;

	VirtNetworkIdentifier createVirtNetworkIdentifier(String id) throws VirtServiceException;

	VirtMachineIdentifier createVirtualMachine(SessionIdentifier sessionIdentifier, VirtMachine virtMachine) throws VirtServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	VirtNetworkIdentifier createVirtNetwork(SessionIdentifier sessionIdentifier, VirtNetwork virtNetwork) throws VirtServiceException, LoginRequiredException, PermissionDeniedException, ValidationException;

	VirtMachineIdentifier createVirtMachineIdentifier(String id) throws VirtServiceException;
}
