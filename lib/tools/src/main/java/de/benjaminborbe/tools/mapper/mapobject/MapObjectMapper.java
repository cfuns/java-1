package de.benjaminborbe.tools.mapper.mapobject;

import de.benjaminborbe.tools.mapper.MapException;

import java.util.Collection;
import java.util.Map;

public interface MapObjectMapper<T> {

	Map<String, String> map(T object) throws MapException;

	void map(T object, Map<String, String> data) throws MapException;

	T map(Map<String, String> data) throws MapException;

	void map(Map<String, String> data, T object) throws MapException;

	Map<String, String> map(T object, Collection<String> fieldNames) throws MapException;

	void map(T object, Map<String, String> data, Collection<String> fieldNames) throws MapException;

	T map(Map<String, String> data, Collection<String> fieldNames) throws MapException;

	void map(Map<String, String> data, T object, Collection<String> fieldNames) throws MapException;

}
