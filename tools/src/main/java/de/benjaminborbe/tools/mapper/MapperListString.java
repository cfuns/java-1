package de.benjaminborbe.tools.mapper;

import java.util.ArrayList;
import java.util.List;

public class MapperListString implements Mapper<List<String>> {

	private static final char SEPERATOR = ',';

	private static final char ESCAPE = '\\';

	@Override
	public String toString(final List<String> values) {
		if (values == null || values.isEmpty()) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final String value : values) {
			if (first) {
				first = false;
			}
			else {
				sb.append(SEPERATOR);
			}
			sb.append(escape(value));
		}
		return sb.toString();
	}

	private String escape(final String value) {
		return value.replaceAll("\\\\", "\\\\\\\\").replaceAll(",", "\\\\,");
	}

	@Override
	public List<String> fromString(final String values) {
		final List<String> result = new ArrayList<String>();
		if (values != null) {
			final char[] chars = values.toCharArray();
			boolean nextEscaped = false;
			final List<Character> value = new ArrayList<Character>();
			for (int i = 0; i < chars.length; ++i) {
				final char c = chars[i];
				if (nextEscaped) {
					nextEscaped = false;
					value.add(c);
				}
				else {
					if (c == ESCAPE) {
						nextEscaped = true;
					}
					else if (c == SEPERATOR) {
						result.add(asString(value));
						value.clear();
					}
					else {
						value.add(c);
					}
				}
			}
			result.add(asString(value));
		}
		return result;
	}

	private String asString(final List<Character> value) {
		final StringBuilder sb = new StringBuilder();
		for (final Character v : value) {
			sb.append(v);
		}
		return sb.toString();
	}
}
