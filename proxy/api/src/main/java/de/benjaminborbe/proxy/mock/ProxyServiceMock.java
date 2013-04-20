package de.benjaminborbe.proxy.mock;

import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;

public class ProxyServiceMock implements ProxyService {

	public ProxyServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}

	@Override
	public void start() throws ProxyServiceException {
	}

	@Override
	public void stop() throws ProxyServiceException {
	}
}
