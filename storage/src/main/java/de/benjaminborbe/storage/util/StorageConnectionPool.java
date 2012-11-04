package de.benjaminborbe.storage.util;

public interface StorageConnectionPool {

	StorageConnection getConnection() throws StorageConnectionPoolException;

	void releaseConnection(StorageConnection connection);

	void close();

	int getFreeConnections();

	int getConnections();

	int getMaxConnections();

}
