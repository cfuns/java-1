package de.benjaminborbe.tools.mapper;

public interface Mapper<T> {

	T fromString(String string);

	String toString(T object);
}
