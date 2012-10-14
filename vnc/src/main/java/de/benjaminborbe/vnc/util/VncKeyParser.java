package de.benjaminborbe.vnc.util;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.vnc.api.VncKey;

public class VncKeyParser {

	private final ParseUtil parseUtil;

	@Inject
	public VncKeyParser(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	public List<VncKey> parseKeys(final String keyString) {
		final List<VncKey> result = new ArrayList<VncKey>();
		final char[] chars = keyString.toCharArray();
		for (int i = 0; i < chars.length; ++i) {
			try {
				result.add(parseKey(chars[i]));
			}
			catch (final ParseException e) {
			}
		}
		return result;
	}

	public VncKey parseKey(final char c) throws ParseException {
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
			return parseUtil.parseEnum(VncKey.class, String.valueOf(c));
		}
	}
}
