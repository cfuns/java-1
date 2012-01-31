package de.benjaminborbe.storage.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StorageService {

	Collection<String> findByIdPrefix(String columnFamily, String prefix) throws StorageException;

	List<String> list(String columnFamily) throws StorageException;

	String get(String columnFamily, String id, String key) throws StorageException;

	void delete(String columnFamily, String id, String key) throws StorageException;

	void set(String columnFamily, String id, String key, String value) throws StorageException;

	void set(String columnFamily, String id, Map<String, String> data) throws StorageException;

}
