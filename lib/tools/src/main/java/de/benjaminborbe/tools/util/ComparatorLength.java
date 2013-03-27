package de.benjaminborbe.tools.util;

public class ComparatorLength extends ComparatorBase<String, Integer> {

	@Override
	public Integer getValue(final String o) {
		return new Integer(o.length());
	}

}
