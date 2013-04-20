package de.benjaminborbe.dhl.util;

import javax.inject.Inject;
import de.benjaminborbe.dhl.api.Dhl;
import org.slf4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;

public class DhlUrlBuilder {

	private final Logger logger;

	@Inject
	public DhlUrlBuilder(final Logger logger) {
		this.logger = logger;
	}

	public URL buildUrl(final Dhl dhl) throws MalformedURLException {
		logger.trace("buildUrl");
		return new URL("http://nolp.dhl.de/nextt-online-public/set_identcodes.do?lang=de&zip=" + dhl.getZip() + "&idc=" + dhl.getTrackingNumber());
	}
}
