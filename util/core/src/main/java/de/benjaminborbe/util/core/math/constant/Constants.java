package de.benjaminborbe.util.core.math.constant;

import de.benjaminborbe.util.core.math.HasValue;

import java.util.HashMap;
import java.util.Map;

public class Constants {

	private final Map<String, HasValue> constants = new HashMap<>();

	public Constants() {
		constants.put("pi", new Constant("3.14159"));
	}

	public boolean exists(final String constantName) {
		return constants.containsKey(constantName.toLowerCase());
	}

	public HasValue get(final String constantName) {
		return constants.get(constantName.toLowerCase());
	}

}
