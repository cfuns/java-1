package de.benjaminborbe.tools.util;

import java.util.UUID;

public class IdGeneratorUUID implements IdGeneratorString {

	@Override
	public String nextId() {
		return String.valueOf(UUID.randomUUID());
	}

}
