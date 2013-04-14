package de.benjaminborbe.dns.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.dns.api.DnsService;

@Singleton
public class DnsServiceMock implements DnsService {

	@Inject
	public DnsServiceMock() {
	}

	@Override
	public long calc(final long value) {
		return 0;
	}
}
