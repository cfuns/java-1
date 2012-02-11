package de.benjaminborbe.tools.mapper;

import java.util.Map;

public interface Mapper<T> {

	Map<String, String> map(T object) throws MapException;

	void map(T object, Map<String, String> data) throws MapException;

	T map(Map<String, String> data) throws MapException;

	void map(Map<String, String> data, T object) throws MapException;
}
