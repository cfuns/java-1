package de.benjaminborbe.storage.tools;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageValue;

import java.util.Collection;
import java.util.Map;

public interface Dao<E extends Entity<? extends I>, I extends Identifier<?>> {

	String getEncoding();

	void save(E entity) throws StorageException;

	void delete(E entity) throws StorageException;

	void delete(I id) throws StorageException;

	E create() throws StorageException;

	E findOrCreate(I id) throws StorageException;

	E load(I id) throws StorageException;

	Collection<E> load(Collection<I> ids) throws StorageException;

	boolean exists(I id) throws StorageException;

	EntityIterator<E> getEntityIterator() throws StorageException;

	IdentifierIterator<I> getIdentifierIterator() throws StorageException;

	IdentifierIterator<I> getIdentifierIterator(Map<StorageValue, StorageValue> where) throws StorageException;

	EntityIterator<E> getEntityIterator(Map<StorageValue, StorageValue> where) throws StorageException;

	void load(E entity, Collection<StorageValue> fieldNames) throws StorageException;

	void save(E entity, Collection<StorageValue> fieldNames) throws StorageException;

	StorageValue buildValue(String content);

}
