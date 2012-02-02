package de.benjaminborbe.tools.dao;

import java.util.Collection;

public interface Dao<E extends Entity<T>, T> {

	void save(E entity);

	void delete(E entity);

	E create();

	E load(T id);

	Collection<E> getAll();

}
