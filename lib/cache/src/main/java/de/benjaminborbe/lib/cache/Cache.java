package de.benjaminborbe.lib.cache;

public interface Cache<Key, Value> {

	Value get(Key configurationDescription);

	void put(Key configurationDescription, Value value);

	void flush();
}
