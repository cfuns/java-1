package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

import de.benjaminborbe.storage.api.StorageException;

public interface StorageDaoUtil {

	void insert(String keySpace, String columnFamily, final String id, final Map<String, String> data) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException;

	String read(String keySpace, String columnFamily, final String id, final String key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException;

	void delete(String keySpace, String columnFamily, final String id, final String key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException;

	StorageKeyIterator keyIterator(String keySpace, String columnFamily) throws InvalidRequestException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, NotFoundException;

	int count(String keySpace, String columnfamily) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException, TException,
			NotFoundException, StorageException;

	int count(String keySpace, String columnfamily, String key) throws UnsupportedEncodingException, InvalidRequestException, UnavailableException, TimedOutException, TException,
			NotFoundException, StorageException;

}
