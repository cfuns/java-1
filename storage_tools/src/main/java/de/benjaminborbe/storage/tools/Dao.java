package de.benjaminborbe.storage.tools;

import java.util.Collection;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;

public interface Dao<E extends Entity<? extends I>, I extends Identifier<?>> {

	void save(E entity) throws StorageException;

	void delete(E entity) throws StorageException;

	E create() throws StorageException;

	E load(I id) throws StorageException;

	Collection<E> getAll() throws StorageException;

}
