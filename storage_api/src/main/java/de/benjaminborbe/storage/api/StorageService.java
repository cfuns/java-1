package de.benjaminborbe.storage.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StorageService {

	StorageIterator findByIdPrefix(String columnFamily, String prefix) throws StorageException;

	StorageIterator list(String columnFamily) throws StorageException;

	StorageIterator list(String columnFamily, Map<String, String> where) throws StorageException;

	String get(String columnFamily, String id, String key) throws StorageException;

	List<String> get(String columnFamily, String id, List<String> keys) throws StorageException;

	void delete(String columnFamily, String id, String key) throws StorageException;

	void delete(String columnFamily, String id, String... keys) throws StorageException;

	void set(String columnFamily, String id, String key, String value) throws StorageException;

	void set(String columnFamily, String id, Map<String, String> data) throws StorageException;

	void delete(String columnFamily, String id, Collection<String> keys) throws StorageException;

	int getFreeConnections();

	int getConnections();

	int getMaxConnections();

}
