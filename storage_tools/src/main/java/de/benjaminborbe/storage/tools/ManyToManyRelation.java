package de.benjaminborbe.storage.tools;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;

public interface ManyToManyRelation<A extends Identifier<?>, B extends Identifier<?>> {

	void add(final A identifierA, final B identifierB) throws StorageException;

	void remove(final A identifierA, final B identifierB) throws StorageException;

	boolean exists(final A identifierA, final B identifierB) throws StorageException;

	void removeB(B identifierB) throws StorageException;

	void removeA(A identifierA) throws StorageException;

	StorageIterator getB(B identifierB) throws StorageException;

	StorageIterator getA(A identifierA) throws StorageException;
}
