package de.benjaminborbe.httpdownloader.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpHeaderDto implements HttpHeader {

	private Map<String, List<String>> header = new HashMap<>();

	@Override
	public String getValue(final String name) {
		if (header.containsKey(name)) {
			List<String> values = header.get(name);
			if (!values.isEmpty()) {
				return values.iterator().next();
			}
		}
		return null;
	}

	@Override
	public List<String> getValues(final String name) {
		if (header.containsKey(name)) {
			return header.get(name);
		}
		return null;
	}

	@Override
	public Collection<String> getKeys() {
		return header.keySet();
	}

	public void setHeader(String name, String value) {
		header.put(name, Arrays.asList(value));
	}

	public void setHeader(String name, List<String> values) {
		header.put(name, values);
	}
}
