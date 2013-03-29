package de.benjaminborbe.virt.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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
}
