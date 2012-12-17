package de.benjaminborbe.tools.mapper.stringobject;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import de.benjaminborbe.tools.mapper.MapException;

public abstract class StringObjectMapperBase<B, T> implements StringObjectMapper<B> {

	private final String name;

	public StringObjectMapperBase(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public abstract String toString(T value);

	public abstract T fromString(String value);

	@SuppressWarnings("unchecked")
	@Override
	public String map(final B bean) throws MapException {
		try {
			return toString((T) PropertyUtils.getProperty(bean, getName()));
		}
		catch (final IllegalAccessException e) {
			throw new MapException(e);
		}
		catch (final InvocationTargetException e) {
			throw new MapException(e);
		}
		catch (final NoSuchMethodException e) {
			throw new MapException(e);
		}
		catch (final ClassCastException e) {
			throw new MapException(e);
		}
	}

	@Override
	public void map(final B bean, final String value) throws MapException {
		try {
			PropertyUtils.setProperty(bean, getName(), fromString(value));
		}
		catch (final IllegalAccessException e) {
			throw new MapException(e);
		}
		catch (final InvocationTargetException e) {
			throw new MapException(e);
		}
		catch (final NoSuchMethodException e) {
			throw new MapException(e);
		}
	}

}
