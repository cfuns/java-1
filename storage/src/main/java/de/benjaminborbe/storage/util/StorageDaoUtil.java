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

	/**
	 * Schreiben eine Map von Daten unter der uebergeben ID in die Datenbank
	 * 
	 * @param keySpace
	 * @param columnFamily
	 * @param id
	 * @param data
	 * @throws InvalidRequestException
	 * @throws UnavailableException
	 * @throws TimedOutException
	 * @throws TException
	 * @throws UnsupportedEncodingException
	 * @throws NotFoundException
	 */
	void insert(String keySpace, String columnFamily, final String id, final Map<String, String> data) throws InvalidRequestException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException, NotFoundException;

	/**
	 * Liest einen Wert mit der uebergeben ID und Key aus der Datenbank
	 * 
	 * @param keySpace
	 * @param columnFamily
	 * @param id
	 * @param key
	 * @return
	 * @throws InvalidRequestException
	 * @throws NotFoundException
	 * @throws UnavailableException
	 * @throws TimedOutException
	 * @throws TException
	 * @throws UnsupportedEncodingException
	 */
	String read(String keySpace, String columnFamily, final String id, final String key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException;

	/**
	 * Loescht einen Wert mit der uebergeben ID und Key aus der Datenbank
	 * 
	 * @param keySpace
	 * @param columnFamily
	 * @param id
	 * @param key
	 * @throws InvalidRequestException
	 * @throws NotFoundException
	 * @throws UnavailableException
	 * @throws TimedOutException
	 * @throws TException
	 * @throws UnsupportedEncodingException
	 */
	void delete(String keySpace, String columnFamily, final String id, final String key) throws InvalidRequestException, NotFoundException, UnavailableException, TimedOutException, TException,
			UnsupportedEncodingException;

	List<String> list(String keySpace, String columnFamily) throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException, NotFoundException;

}
