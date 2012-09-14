package de.benjaminborbe.util.math;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Operations {

	private final Set<String> operations = new HashSet<String>(Arrays.asList("+", "/", "*", "-"));

	public boolean existsByName(final String name) {
		return operations.contains(name);
	}

	public HasValue getByName(final String token, final HasValue valueA, final HasValue valueB) {
		return null;
	}
}
