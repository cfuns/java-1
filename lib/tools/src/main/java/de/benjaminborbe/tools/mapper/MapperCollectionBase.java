package de.benjaminborbe.tools.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapperCollectionBase<T> implements Mapper<Collection<T>> {

	private static final char SEPERATOR = ',';

	private static final char ESCAPE = '\\';

	private final Mapper<T> mapper;

	public MapperCollectionBase(final Mapper<T> mapper) {
		this.mapper = mapper;
	}

	@Override
	public String toString(final Collection<T> values) throws MapException {
		if (values == null || values.isEmpty()) {
			return null;
		}
		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (final T value : values) {
			if (first) {
				first = false;
			} else {
				sb.append(SEPERATOR);
			}
			sb.append(escape(mapper.toString(value)));
		}
		return sb.toString();
	}

	private String escape(final String value) {
		return value.replaceAll("\\\\", "\\\\\\\\").replaceAll(",", "\\\\,");
	}

	@Override
	public List<T> fromString(final String values) throws MapException {
		final List<T> result = new ArrayList<T>();
		if (values != null) {
			final char[] chars = values.toCharArray();
			boolean nextEscaped = false;
			final List<Character> value = new ArrayList<Character>();
			for (int i = 0; i < chars.length; ++i) {
				final char c = chars[i];
				if (nextEscaped) {
					nextEscaped = false;
					value.add(c);
				} else {
					if (c == ESCAPE) {
						nextEscaped = true;
					} else if (c == SEPERATOR) {
						result.add(mapper.fromString(asString(value)));
						value.clear();
					} else {
						value.add(c);
					}
				}
			}
			result.add(mapper.fromString(asString(value)));
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
