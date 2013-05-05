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
		if (line.startsWith("CONNECT ")) {
			final int pos1 = line.indexOf(" ");
			final int pos2 = line.indexOf(":", pos1 + 1);
			return line.substring(pos1 + 1, pos2);
		} else {
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
	}

	public int parsePort(final String line) throws ParseException {
		if (line.startsWith("CONNECT ")) {
			final int pos1 = line.indexOf(":");
			final int pos2 = line.indexOf(" ", pos1 + 1);
			return parseUtil.parseInt(line.substring(pos1 + 1, pos2));
		} else {
			final int pos1 = parseUtil.indexOf(line, "//") + 2;
			int pos = pos1;
			while (pos < line.length()) {
				if (!isValidHostnameChar(line.charAt(pos))) {
					if (line.charAt(pos) == ':') {
						int endpos = pos + 1;
						while (endpos < line.length() && Character.isDigit(line.charAt(endpos))) {
							endpos++;
						}
						return parseUtil.parseInt(line.substring(pos + 1, endpos));
					}
					return 80;
				}
				pos++;
			}
			return 80;
		}
	}

	private boolean isValidHostnameChar(final char c) {
		return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z' || '0' <= c && c <= '9' || c == '.' || c == '-' || 'c' == '_';
		//return Character.isLetterOrDigit(c);
	}

	public String parseUrl(final String line) throws ParseException {
		final int pos1 = line.indexOf(" ");
		if (pos1 == -1) {
			throw new ParseException("parseUrl from line " + line + " failed!");
		}
		final int pos2 = line.indexOf(" ", pos1 + 1);
		if (pos2 != -1) {
			return line.substring(pos1 + 1, pos2);
		} else {
			return line.substring(pos1 + 1);
		}
	}

	public int parseReturnCode(final String line) throws ParseException {
		final int pos1 = line.indexOf(" ");
		final int pos2 = line.indexOf(" ", pos1 + 1);
		return parseUtil.parseInt(line.substring(pos1 + 1, pos2));
	}

	public Map<String, List<String>> parseHeaderLine(final String line) throws ParseException {
		final int pos = line.indexOf(": ");
		if (pos == -1) {
			throw new ParseException("parseHeaderLine from line " + line + " failed!");
		}
		final String key = line.substring(0, pos);
		final String valueString = line.substring(pos + 2);
		final Map<String, List<String>> result = new HashMap<>();
		result.put(key, Arrays.asList(valueString.split(", ")));
		return result;
	}
}
