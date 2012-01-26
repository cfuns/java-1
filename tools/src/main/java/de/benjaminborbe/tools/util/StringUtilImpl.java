package de.benjaminborbe.tools.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
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

}
