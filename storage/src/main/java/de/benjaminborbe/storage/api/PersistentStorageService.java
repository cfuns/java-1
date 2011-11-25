package de.benjaminborbe.storage.api;

import java.util.List;

public interface PersistentStorageService extends StorageService {

	List<String> findByIdPrefix(String columnFamily, String prefix);

	List<String> list(String columnFamily);

}
