package de.benjaminborbe.tools.mapper;

import java.util.List;

public class MapperListBase<T> implements Mapper<List<T>> {

	private final MapperCollectionBase<T> mapper;

	public MapperListBase(final Mapper<T> mapper) {
		this.mapper = new MapperCollectionBase<>(mapper);
	}

	@Override
	public List<T> fromString(final String values) throws MapException {
		return mapper.fromString(values);
	}

	@Override
	public String toString(final List<T> values) throws MapException {
		return mapper.toString(values);
	}

}
