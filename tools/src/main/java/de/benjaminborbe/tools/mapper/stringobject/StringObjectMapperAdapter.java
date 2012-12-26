package de.benjaminborbe.tools.mapper.stringobject;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;

import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;

public class StringObjectMapperAdapter<B, T> implements StringObjectMapper<B> {

	private final String name;

	private final Mapper<T> mapper;

	public StringObjectMapperAdapter(final String name, final Mapper<T> mapper) {
		this.name = name;
		this.mapper = mapper;
	}

	@Override
	public String getName() {
		return name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String map(final B bean) throws MapException {
		try {
			return mapper.toString((T) PropertyUtils.getProperty(bean, getName()));
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
			PropertyUtils.setProperty(bean, getName(), mapper.fromString(value));
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
