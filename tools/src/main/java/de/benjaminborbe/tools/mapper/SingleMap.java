package de.benjaminborbe.tools.mapper;

public interface SingleMap<T> {

	String getName();

	String map(T bean) throws MapException;

	void map(T bean, String value) throws MapException;
}
