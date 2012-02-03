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
import de.benjaminborbe.tools.dao.Entity;

@Singleton
public abstract class StorageDao<T extends Entity<String>> implements Dao<T, String> {

	private final Logger logger;

	private final StorageService storageService;

	private final Provider<T> beanProvider;

	@Inject
	public StorageDao(final Logger logger, final StorageService storageService, final Provider<T> beanProvider) {
		this.logger = logger;
		this.storageService = storageService;
		this.beanProvider = beanProvider;
	}

	@Override
	public void save(final T entity) {
		logger.debug("save");
		for (final String fieldName : getFieldNames(entity)) {
			try {
				final String value = getFieldValue(entity, fieldName);
				storageService.set(getColumnFamily(), getId(entity), fieldName, value);
			}
			catch (final StorageException e) {
				logger.debug("StorageException", e);
			}
			catch (final IllegalAccessException e) {
				logger.debug("IllegalAccessException", e);
			}
			catch (final InvocationTargetException e) {
				logger.debug("InvocationTargetException", e);
			}
			catch (final NoSuchMethodException e) {
				logger.debug("NoSuchMethodException", e);
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
				logger.debug("StorageException", e);
			}
			catch (final IllegalAccessException e) {
				logger.debug("IllegalAccessException", e);
			}
			catch (final InvocationTargetException e) {
				logger.debug("InvocationTargetException", e);
			}
			catch (final NoSuchMethodException e) {
				logger.debug("NoSuchMethodException", e);
			}
		}
	}

	@Override
	public T create() {
		logger.debug("create");
		return beanProvider.get();
	}

	@Override
	public T load(final String id) {
		logger.debug("load");
		final T entity = create();
		for (final String fieldName : getFieldNames(entity)) {
			logger.debug("load fieldName: " + fieldName);
			try {
				final String value = storageService.get(getColumnFamily(), String.valueOf(id), fieldName);
				if ("id".equals(fieldName) && value == null) {
					return null;
				}
				BeanUtils.setProperty(entity, fieldName, value);
			}
			catch (final StorageException e) {
				logger.debug("StorageException", e);
			}
			catch (final IllegalAccessException e) {
				logger.debug("IllegalAccessException", e);
			}
			catch (final InvocationTargetException e) {
				logger.debug("InvocationTargetException", e);
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
				result.add(load(id));
			}
		}
		catch (final StorageException e) {
			logger.debug("StorageException", e);
		}
		return result;
	}

	protected List<String> getFieldNames(final T entity) {
		final List<String> result = new ArrayList<String>();
		try {
			@SuppressWarnings("rawtypes")
			final Map bla = BeanUtils.describe(entity);
			for (final Object key : bla.keySet()) {
				final String name = String.valueOf(key);
				if (!"class".equals(name))
					result.add(name);
			}
		}
		catch (final IllegalAccessException e) {
			logger.debug("IllegalAccessException", e);
		}
		catch (final InvocationTargetException e) {
			logger.debug("InvocationTargetException", e);
		}
		catch (final NoSuchMethodException e) {
			logger.debug("NoSuchMethodException", e);
		}
		return result;
	}
}
