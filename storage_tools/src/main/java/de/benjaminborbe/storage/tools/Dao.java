package de.benjaminborbe.storage.tools;

import java.util.Collection;

import de.benjaminborbe.storage.api.StorageException;

public interface Dao<E extends Entity<T>, T> {

	void save(E entity) throws StorageException;

	void delete(E entity) throws StorageException;

	E create() throws StorageException;

	E load(T id) throws StorageException;

	Collection<E> getAll() throws StorageException;

}
