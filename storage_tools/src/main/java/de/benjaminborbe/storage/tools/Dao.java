package de.benjaminborbe.storage.tools;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;

public interface Dao<E extends Entity<? extends I>, I extends Identifier<?>> {

	void save(E entity) throws StorageException;

	void delete(E entity) throws StorageException;

	void delete(I id) throws StorageException;

	E create() throws StorageException;

	E load(I id) throws StorageException;

	boolean exists(I id) throws StorageException;

	EntityIterator<E> getIterator() throws StorageException;

	IdentifierIterator<I> getIdentifierIterator() throws StorageException;

}
