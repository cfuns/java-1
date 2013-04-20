package de.benjaminborbe.tools.util;

import javax.inject.Inject;

public class ComparatorStringCaseInsensitive extends ComparatorBase<String, String> {

	@Inject
	public ComparatorStringCaseInsensitive() {
	}

	@Override
	public String getValue(final String o) {
		return o.toLowerCase();
	}
}
