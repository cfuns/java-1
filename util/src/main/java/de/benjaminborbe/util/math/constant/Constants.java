package de.benjaminborbe.util.math.constant;

import java.util.HashMap;
import java.util.Map;

import de.benjaminborbe.util.math.HasValue;

public class Constants {

	private final Map<String, HasValue> constants = new HashMap<String, HasValue>();

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
