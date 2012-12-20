package de.benjaminborbe.storage.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StorageService {

	long count(final String columnFamily) throws StorageException;

	long count(final String columnFamily, final String columnName) throws StorageException;

	long count(final String columnFamily, final String columnName, final String columnValue) throws StorageException;

	void delete(String columnFamily, String key, Collection<String> columnNames) throws StorageException;

	void delete(String columnFamily, String key, String columnName) throws StorageException;

	void delete(String columnFamily, String key, String... columnNames) throws StorageException;

	List<String> get(String columnFamily, String key, List<String> columnNames) throws StorageException;

	String get(String columnFamily, String key, String columnName) throws StorageException;

	int getConnections();

	int getFreeConnections();

	int getMaxConnections();

	StorageRowIterator rowIterator(String columnFamily, List<String> columnNames) throws StorageException;

	StorageRowIterator rowIterator(String columnFamily, List<String> columnNames, Map<String, String> where) throws StorageException;

	StorageIterator keyIterator(String columnFamily) throws StorageException;

	StorageIterator keyIterator(String columnFamily, Map<String, String> where) throws StorageException;

	StorageIterator keyIteratorWithPrefix(String columnFamily, String prefix) throws StorageException;

	void set(String columnFamily, String key, Map<String, String> data) throws StorageException;

	void set(String columnFamily, String key, String columnName, String columnValue) throws StorageException;

	void backup() throws StorageException;

	void restore(String columnfamily, String jsonContent) throws StorageException;

}
