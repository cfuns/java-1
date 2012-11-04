package de.benjaminborbe.storage.util;

import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StorageConnectionPoolImpl implements StorageConnectionPool {

	private static final int SOCKET_TIMEOUT = 7000;

	private static final int MAX_CONNECTIONS = 5;

	private final StorageConfig storageConfig;

	private final Logger logger;

	private final BlockingQueue<StorageConnection> freeConnections = new LinkedBlockingQueue<StorageConnection>(MAX_CONNECTIONS);

	private final BlockingQueue<StorageConnection> allConnections = new LinkedBlockingQueue<StorageConnection>(MAX_CONNECTIONS);

	@Inject
	public StorageConnectionPoolImpl(final Logger logger, final StorageConfig storageConfig) {
		this.logger = logger;
		this.storageConfig = storageConfig;
	}

	@Override
	public StorageConnection getConnection() throws StorageConnectionPoolException {
		try {
			if (!freeConnections.isEmpty()) {
				return freeConnections.take();
			}
			else if (allConnections.size() == MAX_CONNECTIONS) {
				throw new StorageConnectionPoolException("max connections reached");
			}
			else {
				final StorageConnection c = createNewConnection();
				allConnections.offer(c);
				return c;
			}
		}
		catch (final TTransportException e) {
			throw new StorageConnectionPoolException(e);
		}
		catch (final SocketException e) {
			throw new StorageConnectionPoolException(e);
		}
		catch (final InterruptedException e) {
			throw new StorageConnectionPoolException(e);
		}
	}

	@Override
	public void releaseConnection(final StorageConnection connection) {
		if (connection == null) {
			logger.debug("can't release connection null");
		}
		else {
			freeConnections.offer(connection);
		}
	}

	@Override
	public void close() {
		while (!allConnections.isEmpty()) {
			try {
				final StorageConnection connection = allConnections.take();
				connection.getTr().close();
			}
			catch (final InterruptedException e) {
			}
		}
		allConnections.clear();
		freeConnections.clear();
	}

	private StorageConnection createNewConnection() throws TTransportException, SocketException {
		logger.debug("createNewConnection to " + storageConfig.getHost() + ":" + storageConfig.getPort());
		final TSocket socket = new TSocket(storageConfig.getHost(), storageConfig.getPort());
		socket.setTimeout(SOCKET_TIMEOUT);
		// socket.getSocket().setReuseAddress(true);
		socket.getSocket().setSoLinger(true, 0);

		final TFramedTransport tr = new TFramedTransport(socket);
		tr.open();
		final TProtocol protocol = new TBinaryProtocol(tr);
		final Client client = new Cassandra.Client(protocol);

		return new StorageConnection(tr, client);
	}

	@Override
	public int getFreeConnections() {
		return freeConnections.size();
	}

	@Override
	public int getConnections() {
		return allConnections.size();
	}

	@Override
	public int getMaxConnections() {
		return MAX_CONNECTIONS;
	}
}
