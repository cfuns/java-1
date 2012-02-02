package de.benjaminborbe.storage.api;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.tools.dao.Dao;
import de.benjaminborbe.tools.dao.EntityLong;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

@Singleton
public abstract class StorageDao<T extends EntityLong> implements Dao<T, Long> {

	private final Logger logger;

	private final StorageService storageService;

	private final ParseUtil parseUtil;

	private final Provider<T> beanProvider;

	@Inject
	public StorageDao(final Logger logger, final StorageService storageService, final ParseUtil parseUtil, final Provider<T> beanProvider) {
		this.logger = logger;
		this.storageService = storageService;
		this.parseUtil = parseUtil;
		this.beanProvider = beanProvider;
	}

	@Override
	public void save(final T entity) {
		logger.debug("save");
		for (final String fieldName : getFieldNames(entity)) {
			try {
				storageService.set(getColumnFamily(), getId(entity), fieldName, getFieldValue(entity, fieldName));
			}
			catch (final StorageException e) {
			}
			catch (final IllegalAccessException e) {
			}
			catch (final InvocationTargetException e) {
			}
			catch (final NoSuchMethodException e) {
			}
		}
	}

	protected String getFieldValue(final T entity, final String fieldName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return BeanUtils.getProperty(entity, fieldName);
	}

	protected String getId(final T entity) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return BeanUtils.getProperty(entity, "id");
	}

	protected String getColumnFamily() {
		return "test";
	}

	@Override
	public void delete(final T entity) {
		logger.debug("delete");
		for (final String fieldName : getFieldNames(entity)) {
			try {
				storageService.delete(getColumnFamily(), getId(entity), fieldName);
			}
			catch (final StorageException e) {
			}
			catch (final IllegalAccessException e) {
			}
			catch (final InvocationTargetException e) {
			}
			catch (final NoSuchMethodException e) {
			}
		}
	}

	@Override
	public T create() {
		logger.debug("create");
		return beanProvider.get();
	}

	@Override
	public T load(final Long id) {
		logger.debug("load");
		final T entity = create();
		for (final String fieldName : getFieldNames(entity)) {
			try {
				final String value = storageService.get(getColumnFamily(), String.valueOf(id), fieldName);
				if ("id".equals(fieldName) && value == null) {
					return null;
				}
				BeanUtils.setProperty(entity, fieldName, value);
			}
			catch (final StorageException e) {
			}
			catch (final IllegalAccessException e) {
			}
			catch (final InvocationTargetException e) {
			}
		}
		return entity;
	}

	@Override
	public Collection<T> getAll() {
		logger.debug("getAll");
		final Set<T> result = new HashSet<T>();
		try {
			for (final String id : storageService.list(getColumnFamily())) {
				result.add(load(parseUtil.parseLong(id)));
			}
		}
		catch (final StorageException e) {
		}
		catch (final ParseException e) {
		}
		return result;
	}

	protected List<String> getFieldNames(final T entity) {
		final List<String> result = new ArrayList<String>();
		try {
			@SuppressWarnings("rawtypes")
			final Map bla = BeanUtils.describe(entity);
			for (final Object o : bla.keySet()) {
				final String name = String.valueOf(o);
				if (!"class".equals(name))
					result.add(name);
			}
		}
		catch (final IllegalAccessException e) {
		}
		catch (final InvocationTargetException e) {
		}
		catch (final NoSuchMethodException e) {
		}
		return result;
	}
}
