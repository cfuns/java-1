package de.benjaminborbe.vnc.util;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncKeyParseException;
import de.benjaminborbe.vnc.api.VncKeyParser;

public class VncKeyParserImpl implements VncKeyParser {

	private final ParseUtil parseUtil;

	private final Logger logger;

	@Inject
	public VncKeyParserImpl(final Logger logger, final ParseUtil parseUtil) {
		this.logger = logger;
		this.parseUtil = parseUtil;
	}

	@Override
	public List<VncKey> parseKeys(final String keyString) throws VncKeyParseException {
		final List<VncKey> result = new ArrayList<VncKey>();
		final char[] chars = keyString.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			result.add(parseKey(chars[i]));
		}
		return result;
	}

	@Override
	public VncKey parseKey(final char c) throws VncKeyParseException {
		if (c == ' ') {
			return VncKey.K_SPACE;
		}
		else if (c == '\n') {
			return VncKey.K_ENTER;
		}
		else if (c == '0') {
			return VncKey.K_0;
		}
		else if (c == '1') {
			return VncKey.K_1;
		}
		else if (c == '2') {
			return VncKey.K_2;
		}
		else if (c == '3') {
			return VncKey.K_3;
		}
		else if (c == '4') {
			return VncKey.K_4;
		}
		else if (c == '5') {
			return VncKey.K_5;
		}
		else if (c == '6') {
			return VncKey.K_6;
		}
		else if (c == '7') {
			return VncKey.K_7;
		}
		else if (c == '8') {
			return VncKey.K_8;
		}
		else if (c == '9') {
			return VncKey.K_9;
		}
		else if (c == '/') {
			return VncKey.K_SLASH;
		}
		else {
			try {
				return parseUtil.parseEnum(VncKey.class, String.valueOf(c));
			}
			catch (final ParseException e) {
				logger.debug("parse key " + c + " failed");
				throw new VncKeyParseException(e);
			}
		}
	}
}
