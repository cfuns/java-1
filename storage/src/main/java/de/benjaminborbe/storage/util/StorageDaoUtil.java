package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

public interface StorageDaoUtil {

	void insert(String keySpace, String columnFamily, final String id, final Map<String, String> data) throws InvalidRequestException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException, NotFoundException;

	String read(String keySpace, String columnFamily, final String id, final String key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException;

	void delete(String keySpace, String columnFamily, final String id, final String key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException,
			TException, UnsupportedEncodingException;

	List<String> list(String keySpace, String columnFamily) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException,
			NotFoundException;

	StorageKeyIterator keyIterator(String keySpace, String columnFamily) throws InvalidRequestException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, NotFoundException;

}
