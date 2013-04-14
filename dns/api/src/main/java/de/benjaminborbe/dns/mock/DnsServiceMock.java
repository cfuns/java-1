package de.benjaminborbe.dns.mock;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.dns.api.DnsService;
import de.benjaminborbe.dns.api.DnsServiceException;

import java.util.Arrays;
import java.util.List;

@Singleton
public class DnsServiceMock implements DnsService {

	@Inject
	public DnsServiceMock() {
	}

	@Override
	public List<String> lookup(final String dnsServer, final String hostname) throws DnsServiceException {
		return Arrays.asList();
	}
}
