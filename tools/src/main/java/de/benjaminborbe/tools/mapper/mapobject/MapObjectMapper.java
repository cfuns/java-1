package de.benjaminborbe.tools.mapper.mapobject;

import java.util.Map;

import de.benjaminborbe.tools.mapper.MapException;

public interface MapObjectMapper<T> {

	Map<String, String> map(T object) throws MapException;

	void map(T object, Map<String, String> data) throws MapException;

	T map(Map<String, String> data) throws MapException;

	void map(Map<String, String> data, T object) throws MapException;
}
