package de.benjaminborbe.storage.util;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransport;

public class StorageConnection {

	private final TTransport tr;

	private final Client client;

	private String keyspace;

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

	public Client getClient(final String keyspace) throws InvalidRequestException, TException {
		setKeyspace(keyspace);
		return getClient();
	}

	public void setKeyspace(final String keyspace) throws InvalidRequestException, TException {
		if (this.keyspace == null || !this.keyspace.equals(keyspace)) {
			client.set_keyspace(keyspace);
			this.keyspace = keyspace;
		}
	}

	public String getKeyspace() {
		return keyspace;
	}

}
