package de.benjaminborbe.dns.mock;

import de.benjaminborbe.dns.api.DnsService;
import de.benjaminborbe.dns.api.DnsServiceException;

import java.util.Arrays;
import java.util.List;

public class DnsServiceMock implements DnsService {

	public DnsServiceMock() {
	}

	@Override
	public List<String> lookup(final String dnsServer, final String hostname) throws DnsServiceException {
		return Arrays.asList();
	}
}
