package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;

public class ProxyUtil {

	private final ParseUtil parseUtil;

	@Inject
	public ProxyUtil(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	public String parseHostname(final String line) throws ParseException {
		final int pos1 = parseUtil.indexOf(line, "//") + 2;
		final int pos2 = line.indexOf("/", pos1);
		final int pos3 = line.indexOf(":", pos1);
		if (pos3 != -1 && pos3 < pos2 || pos3 != -1 && pos2 == -1) {
			return line.substring(pos1, pos3);
		} else if (pos2 != -1) {
			return line.substring(pos1, pos2);
		} else {
			return line.substring(pos1);
		}
	}

	public int parsePort(final String line) throws ParseException {
		final int pos1 = parseUtil.indexOf(line, "//") + 2;
		final int pos2 = line.indexOf(":", pos1);
		if (pos2 == -1) {
			return 80;
		}
		final int pos3 = line.indexOf("/", pos2);
		if (pos3 != -1) {
			return parseUtil.parseInt(line.substring(pos2 + 1, pos3));
		} else {
			return parseUtil.parseInt(line.substring(pos2 + 1));
		}
	}
}
