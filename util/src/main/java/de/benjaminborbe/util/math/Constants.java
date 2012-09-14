package de.benjaminborbe.util.math;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	private final Map<String, HasValue> constants = new HashMap<String, HasValue>();

	public Constants() {
		constants.put("pi", new Constant("3.1"));
	}

	public boolean existsByName(final String name) {
		return constants.containsKey(name.toLowerCase());
	}

	public HasValue getByName(final String name) {
		return constants.get(name.toLowerCase());
	}
}
