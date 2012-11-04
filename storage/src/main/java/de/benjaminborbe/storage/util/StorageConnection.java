package de.benjaminborbe.storage.util;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;

public class StorageConnection {

	private final TTransport tr;

	private final Client client;

	public StorageConnection(final TTransport tr, final Cassandra.Client client) {
		super();
		this.tr = tr;
		this.client = client;
	}

	public TTransport getTr() {
		return tr;
	}

	public Client getClient() {
		return client;
	}

	public Client getClient(final String keySpace) throws InvalidRequestException, TException {
		client.set_keyspace(keySpace);
		return client;
	}

}
