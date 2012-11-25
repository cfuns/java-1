package de.benjaminborbe.storage.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StorageService {

	void delete(String columnFamily, String id, Collection<String> keys) throws StorageException;

	void delete(String columnFamily, String id, String key) throws StorageException;

	void delete(String columnFamily, String id, String... keys) throws StorageException;

	List<String> get(String columnFamily, String id, List<String> keys) throws StorageException;

	String get(String columnFamily, String id, String key) throws StorageException;

	int getConnections();

	int getFreeConnections();

	int getMaxConnections();

	StorageRowIterator rowIterator(String columnFamily, List<String> columnNames) throws StorageException;

	StorageRowIterator rowIterator(String columnFamily, List<String> columnNames, Map<String, String> where) throws StorageException;

	StorageIterator keyIterator(String columnFamily) throws StorageException;

	StorageIterator keyIterator(String columnFamily, Map<String, String> where) throws StorageException;

	StorageIterator keyIteratorWithPrefix(String columnFamily, String prefix) throws StorageException;

	void set(String columnFamily, String id, Map<String, String> data) throws StorageException;

	void set(String columnFamily, String id, String key, String value) throws StorageException;

	void backup() throws StorageException;

}
