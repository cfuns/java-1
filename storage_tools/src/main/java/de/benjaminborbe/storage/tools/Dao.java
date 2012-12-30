package de.benjaminborbe.storage.tools;

import java.util.Collection;
import java.util.Map;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;

public interface Dao<E extends Entity<? extends I>, I extends Identifier<?>> {

	void save(E entity) throws StorageException;

	void delete(E entity) throws StorageException;

	void delete(I id) throws StorageException;

	E create() throws StorageException;

	E load(I id) throws StorageException;

	boolean exists(I id) throws StorageException;

	EntityIterator<E> getEntityIterator() throws StorageException;

	IdentifierIterator<I> getIdentifierIterator() throws StorageException;

	IdentifierIterator<I> getIdentifierIterator(Map<String, String> where) throws StorageException;

	EntityIterator<E> getEntityIterator(Map<String, String> where) throws StorageException;

	void load(E entity, Collection<String> fieldNames) throws StorageException;

	void save(E entity, Collection<String> fieldNames) throws StorageException;

}
