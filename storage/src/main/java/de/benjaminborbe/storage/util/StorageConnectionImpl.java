package de.benjaminborbe.storage.util;

import java.net.SocketException;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StorageConnectionImpl implements StorageConnection {

	private static final int SOCKET_TIMEOUT = 7000;

	private final ThreadLocal<Client> clientThreadLocal = new ThreadLocal<Client>();

	private final ThreadLocal<TFramedTransport> trThreadLocal = new ThreadLocal<TFramedTransport>();

	private final StorageConfig config;

	private final Logger logger;

	@Inject
	public StorageConnectionImpl(final Logger logger, final StorageConfig config) {
		this.logger = logger;
		this.config = config;
	}

	@Override
	public void open() throws TTransportException, SocketException {
		logger.trace("open cassandra connection " + config.getHost() + ":" + config.getPort());

		final TSocket socket = new TSocket(config.getHost(), config.getPort());
		socket.setTimeout(SOCKET_TIMEOUT);
		socket.getSocket().setReuseAddress(true);
		socket.getSocket().setSoLinger(true, 0);

		final TFramedTransport tr = new TFramedTransport(socket);
		trThreadLocal.set(tr);
		tr.open();
		final TProtocol protocol = new TBinaryProtocol(tr);
		final Client client = new Cassandra.Client(protocol);
		clientThreadLocal.set(client);
	}

	@Override
	public void close() {
		logger.trace("close cassandra connection");
		final TFramedTransport tr = trThreadLocal.get();
		trThreadLocal.remove();
		clientThreadLocal.remove();
		if (tr != null)
			tr.close();
	}

	@Override
	public Client getClient() throws StorageConnectionClosedException {
		return clientThreadLocal.get();
	}

	@Override
	public TFramedTransport getTr() {
		return trThreadLocal.get();
	}
}
