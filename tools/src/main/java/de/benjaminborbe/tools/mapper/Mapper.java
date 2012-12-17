package de.benjaminborbe.tools.mapper;

public interface Mapper<T> {

	T map(String string);

	String map(T object);
}
