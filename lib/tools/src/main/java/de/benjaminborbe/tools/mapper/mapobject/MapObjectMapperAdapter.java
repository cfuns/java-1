package de.benjaminborbe.tools.mapper.mapobject;

import com.google.inject.Provider;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.stringobject.StringObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MapObjectMapperAdapter<T> extends MapObjectMapperBase<T> {

	private final List<StringObjectMapper<T>> mappings;

	public MapObjectMapperAdapter(final Provider<T> provider, final StringObjectMapper<T>... mapSingles) {
		this(provider, Arrays.asList(mapSingles));
	}

	public MapObjectMapperAdapter(final Provider<T> provider, final Collection<StringObjectMapper<T>> mapSingles) {
		super(provider);
		mappings = new ArrayList<StringObjectMapper<T>>(mapSingles);
	}

	@Override
	public void map(final T object, final Map<String, String> data) throws MapException {
		for (final StringObjectMapper<T> m : mappings) {
			data.put(m.getName(), m.map(object));
		}
	}

	@Override
	public void map(final Map<String, String> data, final T object) throws MapException {
		for (final StringObjectMapper<T> m : mappings) {
			if (data.containsKey(m.getName())) {
				m.map(object, data.get(m.getName()));
			}
		}
	}

	@Override
	public void map(final T object, final Map<String, String> data, final Collection<String> fieldNames) throws MapException {
		final Set<String> fields = toSet(fieldNames);
		for (final StringObjectMapper<T> m : mappings) {
			if (fields.contains(m.getName())) {
				data.put(m.getName(), m.map(object));
			}
		}
	}

	@Override
	public void map(final Map<String, String> data, final T object, final Collection<String> fieldNames) throws MapException {
		final Set<String> fields = toSet(fieldNames);
		for (final StringObjectMapper<T> m : mappings) {
			if (fields.contains(m.getName()) && data.containsKey(m.getName())) {
				m.map(object, data.get(m.getName()));
			}
		}
	}

	private Set<String> toSet(final Collection<String> fieldNames) {
		return new HashSet<String>(fieldNames);
	}
}
