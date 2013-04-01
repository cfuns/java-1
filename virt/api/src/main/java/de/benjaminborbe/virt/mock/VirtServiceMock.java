package de.benjaminborbe.virt.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.virt.api.VirtMachineIdentifier;
import de.benjaminborbe.virt.api.VirtNetwork;
import de.benjaminborbe.virt.api.VirtNetworkIdentifier;
import de.benjaminborbe.virt.api.VirtService;
import de.benjaminborbe.virt.api.VirtServiceException;

@Singleton
public class VirtServiceMock implements VirtService {

	@Inject
	public VirtServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}

	@Override
	public VirtNetworkIdentifier createVirtNetworkIdentifier(final String id) throws VirtServiceException {
		if (id == null || id.trim().isEmpty()) {
			return null;
		} else {
			return new VirtNetworkIdentifier(id);
		}
	}

	@Override
	public VirtMachineIdentifier createVirtualMachine() {
		return null;
	}

	@Override
	public VirtNetworkIdentifier createVirtNetwork(final VirtNetwork virtNetwork) throws VirtServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public VirtMachineIdentifier createVirtMachineIdentifier(final String id) throws VirtServiceException {
		return null;
	}
}
