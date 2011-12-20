package de.benjaminborbe.storage.api;

import java.util.Collection;
import java.util.List;

public interface PersistentStorageService extends StorageService {

	Collection<String> findByIdPrefix(String columnFamily, String prefix) throws StorageException;

	List<String> list(String columnFamily) throws StorageException;

}
