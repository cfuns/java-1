package de.benjaminborbe.proxy.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;

@Singleton
public class ProxyServiceMock implements ProxyService {

	@Inject
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
