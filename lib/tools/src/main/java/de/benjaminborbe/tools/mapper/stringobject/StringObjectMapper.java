package de.benjaminborbe.tools.mapper.stringobject;

import de.benjaminborbe.tools.mapper.MapException;

public interface StringObjectMapper<T> {

	String getName();

	String map(T bean) throws MapException;

	void map(T bean, String value) throws MapException;
}
