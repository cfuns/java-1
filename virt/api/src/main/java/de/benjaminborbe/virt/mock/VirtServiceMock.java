package de.benjaminborbe.virt.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.virt.api.VirtService;

@Singleton
public class VirtServiceMock implements VirtService {

	@Inject
	public VirtServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}
}
