package de.benjaminborbe.dhl.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.dhl.api.DhlIdentifier;

public class DhlUrlBuilder {

	private final Logger logger;

	@Inject
	public DhlUrlBuilder(final Logger logger) {
		this.logger = logger;
	}

	public URL buildUrl(final DhlIdentifier dhlIdentifier) throws MalformedURLException {
		logger.debug("buildUrl");
		return new URL("http://nolp.dhl.de/nextt-online-public/set_identcodes.do?lang=de&zip=" + dhlIdentifier.getZip() + "&idc=" + dhlIdentifier.getId());
	}
}
