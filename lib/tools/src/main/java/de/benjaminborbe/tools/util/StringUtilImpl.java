package de.benjaminborbe.tools.util;

import com.google.inject.Inject;

public class StringUtilImpl implements StringUtil {

	@Inject
	public StringUtilImpl() {
	}

	@Override
	public String shorten(final String string, final int length) {
		if (string != null && string.length() > length) {
			return string.substring(0, length);
		}
		return string;
	}

	@Override
	public String shortenDots(final String string, final int length) {
		if (string != null && string.length() > length) {
			return string.substring(0, length) + "...";
		}
		return string;
	}

	@Override
	public String trim(final String string) {
		return string != null ? string.trim() : null;
	}
}
