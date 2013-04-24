package de.benjaminborbe.dhl.util;

import de.benjaminborbe.dhl.api.Dhl;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.net.URL;

public class DhlUrlBuilder {

	private final Logger logger;

	private final ParseUtil parseUtil;

	@Inject
	public DhlUrlBuilder(final Logger logger, final ParseUtil parseUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	public URL buildUrl(final Dhl dhl) throws ParseException {
		logger.trace("buildUrl");
		return parseUtil.parseURL("http://nolp.dhl.de/nextt-online-public/set_identcodes.do?lang=de&zip=" + dhl.getZip() + "&idc=" + dhl.getTrackingNumber());
	}
}
