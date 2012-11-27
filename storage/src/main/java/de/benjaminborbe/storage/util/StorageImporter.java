package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.slf4j.Logger;

import com.google.inject.Inject;

public class StorageImporter {

	private final Logger logger;

	private final StorageConnectionPool storageConnectionPool;

	@Inject
	public StorageImporter(final Logger logger, final StorageConnectionPool storageConnectionPool) {
		this.logger = logger;
		this.storageConnectionPool = storageConnectionPool;
	}

	public void importJson(final String keySpace, final String columnFamily, final String jsonContent) throws StorageConnectionPoolException, InvalidRequestException, TException,
			UnsupportedEncodingException, UnavailableException, TimedOutException {
		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			logger.info("importJson - keySpace: " + keySpace + " columnfamily: " + columnFamily);

			final ColumnParent column_parent = new ColumnParent(columnFamily);
			final String encoding = "UTF-8";

			// TODO bborbe
			final byte[] key = null;
			final String columnName = null;
			final String columnValue = null;
			final long timestamp = 0;

			final Column column = new Column(ByteBuffer.wrap(columnName.getBytes(encoding)));
			column.setValue(ByteBuffer.wrap(columnValue.getBytes(encoding)));
			column.setTimestamp(timestamp);
			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

			client.insert(ByteBuffer.wrap(key), column_parent, column, consistency_level);

		}
		finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}
}
