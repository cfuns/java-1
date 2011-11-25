package de.benjaminborbe.storage.api;

import java.util.Map;

public interface StorageService {

	String get(String columnFamily, String id, String key);

	void delete(String columnFamily, String id, String key);

	void set(String columnFamily, String id, String key, String value);

	void set(String columnFamily, String id, Map<String, String> data);

}
