package de.benjaminborbe.tools.mapper;

import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

import javax.inject.Inject;

public abstract class MapperEnum<E extends Enum<E>> implements Mapper<E> {

	private final ParseUtil parseUtil;

	@Inject
	public MapperEnum(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	protected abstract Class<E> getEnumClass();

	@Override
	public E fromString(final String value) throws MapException {
		try {
			return value != null ? parseUtil.parseEnum(getEnumClass(), value) : null;
		} catch (final ParseException e) {
			throw new MapException(e);
		}
	}

	@Override
	public String toString(final E object) throws MapException {
		return object != null ? object.name() : null;
	}
}
