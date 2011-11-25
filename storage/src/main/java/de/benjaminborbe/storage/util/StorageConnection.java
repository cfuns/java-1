package de.benjaminborbe.storage.util;

import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public interface StorageConnection {

	void open() throws TTransportException;

	void close();

	TTransport getTr();

	Client getClient();
}
