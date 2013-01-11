package de.benjaminborbe.storage.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StorageService {

	String getEncoding();

	void backup() throws StorageException;

	long count(final String columnFamily) throws StorageException;

	long count(final String columnFamily, final StorageValue columnName) throws StorageException;

	long count(final String columnFamily, final StorageValue columnName, final StorageValue columnValue) throws StorageException;

	void delete(String columnFamily, StorageValue key) throws StorageException;

	void delete(String columnFamily, StorageValue key, Collection<StorageValue> columnNames) throws StorageException;

	void delete(String columnFamily, StorageValue key, StorageValue columnName) throws StorageException;

	Map<StorageValue, StorageValue> get(String columnFamily, StorageValue key) throws StorageException;

	List<StorageValue> get(String columnFamily, StorageValue key, List<StorageValue> columnNames) throws StorageException;

	Collection<List<StorageValue>> get(String columnFamily, Collection<StorageValue> key, List<StorageValue> columnNames) throws StorageException;

	StorageValue get(String columnFamily, StorageValue key, StorageValue columnName) throws StorageException;

	int getConnections();

	int getFreeConnections();

	int getMaxConnections();

	StorageColumnIterator columnIterator(String columnFamily, StorageValue key) throws StorageException;

	StorageIterator keyIterator(String columnFamily) throws StorageException;

	StorageIterator keyIterator(String columnFamily, Map<StorageValue, StorageValue> where) throws StorageException;

	void restore(String columnfamily, String jsonContent) throws StorageException;

	StorageRowIterator rowIterator(String columnFamily, List<StorageValue> columnNames) throws StorageException;

	StorageRowIterator rowIterator(String columnFamily, List<StorageValue> columnNames, Map<StorageValue, StorageValue> where) throws StorageException;

	void set(String columnFamily, StorageValue key, Map<StorageValue, StorageValue> data) throws StorageException;

	void set(String columnFamily, StorageValue key, StorageValue columnName, StorageValue columnValue) throws StorageException;

	void backup(String columnFamily) throws StorageException;

}
