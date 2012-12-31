package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
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

public interface StorageDaoUtil {

	long count(String keySpace, String columnFamily) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException, TException,
			NotFoundException, StorageException;

	long count(String keySpace, String columnFamily, String columnName) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException,
			TException, NotFoundException, StorageException, SocketException, StorageConnectionPoolException;

	long count(String keySpace, String columnFamily, String columnName, String columnValue) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException,
			TimedOutException, TException, NotFoundException, StorageException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, final byte[] key, final List<String> columnNames) throws InvalidRequestException, NotFoundException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, byte[] key, String columnName) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, final String key, final List<String> columnNames) throws InvalidRequestException, NotFoundException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, String key, String columnName) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, byte[] key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void delete(String keySpace, String columnFamily, String key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	void insert(String keySpace, String columnFamily, final byte[] key, final Map<String, String> data) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException;

	void insert(String keySpace, String columnFamily, byte[] id, String columnName, String columnValue) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException;

	void insert(String keySpace, String columnFamily, final String key, final Map<String, String> data) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException;

	void insert(String keySpace, String columnFamily, String id, String columnName, String columnValue) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException, SocketException, StorageConnectionPoolException;

	StorageIterator keyIterator(String keySpace, String columnFamily) throws InvalidRequestException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, NotFoundException;

	StorageIterator keyIterator(String keySpace, String columnFamily, Map<String, String> where) throws InvalidRequestException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, NotFoundException, StorageConnectionPoolException;

	List<String> read(String keySpace, String columnFamily, final byte[] key, final List<String> columnNames) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	String read(String keySpace, String columnFamily, byte[] key, String columnName) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	List<String> read(String keySpace, String columnFamily, final String key, final List<String> columnNames) throws InvalidRequestException, NotFoundException,
			UnavailableException, TimedOutException, TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	String read(String keySpace, String columnFamily, String key, String columnName) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, SocketException, StorageConnectionPoolException;

	StorageRowIterator rowIterator(String keySpace, List<String> columnNames, String columnFamily, Map<String, String> where) throws InvalidRequestException, UnavailableException,
			TimedOutException, TException, UnsupportedEncodingException, NotFoundException;

	StorageRowIterator rowIterator(String keySpace, String columnFamily, List<String> columnNames) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException;

	Map<String, String> read(String keySpace, String columnFamily, String id) throws NotFoundException, UnsupportedEncodingException, StorageConnectionPoolException,
			InvalidRequestException, TException, UnavailableException, TimedOutException;

	Map<String, String> read(String keySpace, String columnFamily, byte[] id) throws NotFoundException, StorageConnectionPoolException, InvalidRequestException, TException,
			UnavailableException, TimedOutException, UnsupportedEncodingException;

	StorageColumnIterator columnIterator(String keySpace, String columnFamily, byte[] key);

	StorageColumnIterator columnIterator(String keySpace, String columnFamily, String key) throws UnsupportedEncodingException;

}
