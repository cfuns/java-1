package de.benjaminborbe.storage.api;

import java.util.Map;

public interface StorageService {

	String get(String columnFamily, String id, String key) throws StorageException;

	void delete(String columnFamily, String id, String key) throws StorageException;

	void set(String columnFamily, String id, String key, String value) throws StorageException;

	void set(String columnFamily, String id, Map<String, String> data) throws StorageException;

}
