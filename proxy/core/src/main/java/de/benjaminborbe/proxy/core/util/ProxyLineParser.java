package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyLineParser {

	private final ParseUtil parseUtil;

	@Inject
	public ProxyLineParser(final ParseUtil parseUtil) {
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

	public String parseUrl(final String line) throws ParseException {
		int pos = line.indexOf(" ");
		if (pos != -1) {
			return line.substring(pos + 1);
		} else {
			throw new ParseException("parseUrl from line " + line + " failed!");
		}
	}

	public int parseReturnCode(final String line) throws ParseException {
		int pos1 = line.indexOf(" ");
		int pos2 = line.indexOf(" ", pos1 + 1);
		return parseUtil.parseInt(line.substring(pos1 + 1, pos2));
	}

	public Map<String, List<String>> parseHeaderLine(final String line) {
		int pos = line.indexOf(": ");
		String key = line.substring(0, pos);
		String valueString = line.substring(pos + 2);
		Map<String, List<String>> result = new HashMap<>();
		result.put(key, Arrays.asList(valueString.split(", ")));
		return result;
	}
}
