package de.benjaminborbe.dns.core.service;

import javax.inject.Inject;
import javax.inject.Singleton;
import de.benjaminborbe.dns.api.DnsService;
import de.benjaminborbe.dns.api.DnsServiceException;
import org.slf4j.Logger;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Cache;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.TXTRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DnsCoreServiceImpl implements DnsService {

	private final Logger logger;

	@Inject
	public DnsCoreServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public List<String> lookup(final String dnsServer, final String hostname) throws DnsServiceException {
		try {
			final List<String> result = new ArrayList<>();
			logger.debug("lookup - server: " + dnsServer + " host: " + hostname);
			final Lookup lookup = new Lookup(hostname, Type.ANY);
			lookup.setResolver(new SimpleResolver(dnsServer));
			final Cache cache = new Cache();
			cache.setMaxEntries(0);
			cache.clearCache();
			lookup.setCache(cache);
			lookup.run();
			if (lookup.getResult() == Lookup.SUCCESSFUL) {
				final Record[] records = lookup.run();
				for (final Record record : records) {
					if (record instanceof TXTRecord) {
						final TXTRecord txt = (TXTRecord) record;

						for (final Object o : txt.getStrings()) {
							logger.debug("text" + o);
						}
					} else if (record instanceof ARecord) {
						result.add(((ARecord) record).getAddress().getHostAddress());
					}
				}
			}
			return result;
		} catch (TextParseException | UnknownHostException e) {
			throw new DnsServiceException(e);
		}
	}
}
