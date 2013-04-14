package de.benjaminborbe.dns.api;

import java.util.List;

public interface DnsService {

	List<String> lookup(String dnsServer, String hostname) throws DnsServiceException;
}
