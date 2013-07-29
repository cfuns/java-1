package de.benjaminborbe.tools.mapper.stringobject;

import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

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
		} catch (ClassCastException e) {
			throw new MapException(e);
		} catch (NoSuchMethodException e) {
			throw new MapException(e);
		} catch (InvocationTargetException e) {
			throw new MapException(e);
		} catch (IllegalAccessException e) {
			throw new MapException(e);
		}
	}

	@Override
	public void map(final B bean, final String value) throws MapException {
		try {
			PropertyUtils.setProperty(bean, getName(), mapper.fromString(value));
		} catch (final InvocationTargetException e) {
			throw new MapException(e);
		} catch (NoSuchMethodException e) {
			throw new MapException(e);
		} catch (IllegalAccessException e) {
			throw new MapException(e);
		}
	}

}
