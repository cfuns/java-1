package de.benjaminborbe.util.core.math.function;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Functions {

	private final Set<String> functions = new HashSet<>(Arrays.asList("add"));

	public boolean existsByName(final String name) {
		return functions.contains(name);
	}
}
