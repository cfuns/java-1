package de.benjaminborbe.util.math.function;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Functions {

	private final Set<String> functions = new HashSet<String>(Arrays.asList("add"));

	public boolean existsByName(final String name) {
		return functions.contains(name);
	}
}
