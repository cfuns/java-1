package de.benjaminborbe.tools.util;

import com.google.inject.Inject;

public class ComparatorStringCaseInsensitive extends ComparatorBase<String, String> {

	@Inject
	public ComparatorStringCaseInsensitive() {
	}

	@Override
	public String getValue(final String o) {
		return o.toLowerCase();
	}
}
