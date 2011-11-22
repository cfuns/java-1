package de.benjaminborbe.index.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IdGeneratorImpl implements IdGenerator {

	private long id;

	@Inject
	public IdGeneratorImpl() {
	}

	@Override
	public synchronized long nextId() {
		id++;
		return id;
	}
}
