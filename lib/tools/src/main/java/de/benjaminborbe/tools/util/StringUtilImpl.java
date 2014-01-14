package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		if (string == null) {
			return null;
		}
		final Pattern pattern = Pattern.compile("[\\s\\n\\t]*(.*?)[\\s\\n\\t]*");
		final Matcher matcher = pattern.matcher(string);
		if (!matcher.matches()) {
			return null;
		}
		return matcher.group(1);
	}
}
