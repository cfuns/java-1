package de.benjaminborbe.tools.util;

import java.util.UUID;

public class IdGeneratorUUID implements IdGeneratorString {

	@Override
	public String nextId() {
		return String.valueOf(UUID.randomUUID());
	}

	public String nextId(final byte[] content) {
		return String.valueOf(UUID.nameUUIDFromBytes(content));
	}

	public String nextId(final String content) {
		return nextId(content.getBytes());
	}

}
