package de.benjaminborbe.tools.util;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IdGeneratorLongImpl implements IdGeneratorLong {

	private Long id = 0L;

	@Inject
	public IdGeneratorLongImpl() {
	}

	@Override
	public synchronized Long nextId() {
		id++;
		return id;
	}
}
