package de.benjaminborbe.tools.util;

public class ComparatorStringCaseInsensitive extends ComparatorBase<String, String> {

	@Override
	public String getValue(final String o) {
		return o.toLowerCase();
	}
}
