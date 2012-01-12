package de.benjaminborbe.storage.util;

import java.net.SocketException;

import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public interface StorageConnection {

	void open() throws TTransportException, SocketException;

	void close();

	TTransport getTr();

	Client getClient();
}
