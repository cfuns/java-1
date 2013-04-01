package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageValue;

public interface StorageDaoUtil {

	long count(String keySpace, String columnFamily) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException, TException,
			NotFoundException, StorageException;

	long count(String keySpace, String columnFamily, StorageValue columnName) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException,
			TException, NotFoundException, StorageException, SocketException, StorageConnectionPoolException;

	long count(String keySpace, String columnFamily, StorageValue columnName, StorageValue columnValue) throws UnsupportedEncodingException, InvalidRequestException,
			UnavailableException, TimedOutException, TException, NotFoundException, StorageException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, final StorageValue key, final Collection<StorageValue> columnNames) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, StorageValue key, StorageValue columnName) throws InvalidRequestException, NotFoundException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, StorageValue key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void insert(String keySpace, String columnFamily, final StorageValue key, final Map<StorageValue, StorageValue> data) throws InvalidRequestException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException;

	void insert(String keySpace, String columnFamily, StorageValue id, StorageValue columnName, StorageValue columnValue) throws InvalidRequestException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException;

	StorageIterator keyIterator(String keySpace, String columnFamily) throws InvalidRequestException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, NotFoundException;

	StorageIterator keyIterator(String keySpace, String columnFamily, Map<StorageValue, StorageValue> where) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException, StorageConnectionPoolException;

	List<StorageValue> read(String keySpace, String columnFamily, final StorageValue key, final List<StorageValue> columnNames) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	StorageValue read(String keySpace, String columnFamily, StorageValue key, StorageValue columnName) throws InvalidRequestException, NotFoundException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	StorageRowIterator rowIterator(String keySpace, String columnFamily, List<StorageValue> columnNames, Map<StorageValue, StorageValue> where) throws InvalidRequestException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException;

	StorageRowIterator rowIterator(String keySpace, String columnFamily, List<StorageValue> columnNames) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException;

	Map<StorageValue, StorageValue> read(String keySpace, String columnFamily, StorageValue id) throws NotFoundException, UnsupportedEncodingException,
			StorageConnectionPoolException, InvalidRequestException, TException, UnavailableException, TimedOutException;

	StorageColumnIterator columnIterator(String keySpace, String columnFamily, StorageValue key) throws UnsupportedEncodingException;

	Collection<List<StorageValue>> read(String keySpace, String columnFamily, Collection<StorageValue> keys, List<StorageValue> columnNames) throws UnsupportedEncodingException,
			InvalidRequestException, UnavailableException, TimedOutException, TException, StorageConnectionPoolException;

	StorageColumnIterator columnIteratorReversed(String keySpace, String columnFamily, StorageValue key) throws UnsupportedEncodingException;

}
