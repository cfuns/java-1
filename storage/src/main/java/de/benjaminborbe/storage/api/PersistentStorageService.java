package de.benjaminborbe.storage.api;

import java.util.Collection;
import java.util.List;

public interface PersistentStorageService extends StorageService {

	Collection<String> findByIdPrefix(String columnFamily, String prefix);

	List<String> list(String columnFamily);

}
