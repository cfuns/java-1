package de.benjaminborbe.tools.mapper;

public interface Mapper<T> {

	T fromString(String string) throws MapException;

	String toString(T object) throws MapException;
}
