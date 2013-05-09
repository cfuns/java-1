package de.benjaminborbe.httpdownloader.tools;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHeaderDto implements HttpHeader {

	private final Map<String, List<String>> header = new HashMap<>();

	@Override
	public String getValue(final String name) {
		if (header.containsKey(name)) {
			final List<String> values = header.get(name);
			if (values != null && !values.isEmpty()) {
				return values.get(0);
			}
		}
		return null;
	}

	@Override
	public List<String> getValues(final String key) {
		return header.get(key);
	}

	@Override
	public Collection<String> getKeys() {
		return header.keySet();
	}

	public void setHeader(final String name, final List<String> values) {
		header.put(name, values);
	}

	public void setHeader(final String name, final String value) {
		setHeader(name, Arrays.asList(value));
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final HttpHeaderDto that = (HttpHeaderDto) o;

		if (header.size() != that.header.size()) return false;

		for (final Map.Entry<String, List<String>> e : header.entrySet()) {
			final List<String> thatValue = that.header.get(e.getKey());
			final List<String> thisValue = e.getValue();
			if (!(thisValue == null && thatValue == null || thisValue.equals(thatValue))) return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return header != null ? header.hashCode() : 0;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("header", header)
			.toString();
	}

}
