package de.benjaminborbe.tools.dao;

import java.util.Collection;

public interface Dao<T extends Entity> {

	void save(T entity);

	void delete(T entity);

	T create();

	T load(long id);

	Collection<T> getAll();

}
