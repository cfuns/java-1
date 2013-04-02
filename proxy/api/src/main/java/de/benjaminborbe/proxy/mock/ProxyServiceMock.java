package de.benjaminborbe.proxy.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.proxy.api.ProxyService;

@Singleton
public class ProxyServiceMock implements ProxyService {

	@Inject
	public ProxyServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}
}
