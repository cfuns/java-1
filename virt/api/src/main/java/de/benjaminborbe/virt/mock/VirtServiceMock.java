package de.benjaminborbe.virt.mock;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.virt.api.VirtMachine;
import de.benjaminborbe.virt.api.VirtMachineIdentifier;
import de.benjaminborbe.virt.api.VirtNetwork;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.api.VirtService;
import de.benjaminborbe.virt.api.VirtServiceException;
import de.benjaminborbe.virt.api.VirtVirtualMachineIdentifier;

@Singleton
public class VirtServiceMock implements VirtService {

	@Inject
	public VirtServiceMock() {
	}

	@Override
	public VirtNetworkIdentifier createNetworkIdentifier(final String id) {
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new VirtNetworkIdentifier(id);
		}
	}

	@Override
	public VirtNetworkIdentifier createNetwork(final SessionIdentifier sessionIdentifier, final VirtNetwork network) throws VirtServiceException, PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public VirtMachineIdentifier createMachineIdentifier(final String id) {
		return null;
	}

	@Override
	public VirtMachineIdentifier createMachine(final SessionIdentifier sessionIdentifier, final VirtMachine machine) throws VirtServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public VirtVirtualMachineIdentifier createVirtualMachineIdentifier(final String id) {
		return null;
	}

	@Override
	public VirtVirtualMachineIdentifier createVirtualMachine(final SessionIdentifier sessionIdentifier, final VirtMachine machine) throws VirtServiceException, PermissionDeniedException {
		return null;
	}

	@Override
	public VirtNetwork getNetwork(final SessionIdentifier sessionIdentifier, final VirtNetworkIdentifier networkIdentifier) throws VirtServiceException, PermissionDeniedException {
		return null;
	}
}
