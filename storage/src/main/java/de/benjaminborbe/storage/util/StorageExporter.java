package de.benjaminborbe.storage.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.util.List;

import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KsDef;
import org.apache.cassandra.thrift.NotFoundException;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.json.simple.JSONArray;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.storage.api.StorageException;

public class StorageExporter {

	private final StorageConnectionPool storageConnectionPool;

	private final Logger logger;

	@Inject
	public StorageExporter(final Logger logger, final StorageConnectionPool storageConnectionPool) {
		this.logger = logger;
		this.storageConnectionPool = storageConnectionPool;
	}

	public void export(final File targetDirectory, final String keyspace) throws StorageConnectionPoolException, InvalidRequestException, TException, NotFoundException,
			StorageException, UnavailableException, TimedOutException, IOException {
		if (!targetDirectory.exists()) {
			throw new StorageException("targetdirectory not exists");
		}
		if (!targetDirectory.isDirectory()) {
			throw new StorageException("targetdirectory not a directory");
		}
		if (!targetDirectory.canWrite()) {
			throw new StorageException("targetdirectory not writeable");
		}

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keyspace);

			final KsDef ks = client.describe_keyspace(keyspace);
			for (final CfDef cf : ks.getCf_defs()) {
				final String file = targetDirectory.getAbsolutePath() + "/" + cf.getName() + ".json";
				final FileWriter fileWriter = new FileWriter(file);
				export(fileWriter, keyspace, cf.getName());
				fileWriter.close();
			}
		}
		finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@SuppressWarnings("unchecked")
	public void export(final Writer sw, final String keyspace, final String columnFamily) throws StorageConnectionPoolException, StorageException, InvalidRequestException,
			UnavailableException, TimedOutException, TException, NotFoundException, IOException {

		sw.append("{\n");

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keyspace);

			boolean first = true;
			final StorageKeyIteratorImpl i = new StorageKeyIteratorImpl(storageConnectionPool, keyspace, columnFamily, "UTF-8");
			while (i.hasNext()) {
				final byte[] key = i.nextByte();

				final SlicePredicate predicate = new SlicePredicate();
				predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]), ByteBuffer.wrap(new byte[0]), false, 10000));
				final ColumnParent column_parent = new ColumnParent();
				column_parent.setColumn_family(columnFamily);
				final List<ColumnOrSuperColumn> results = client.get_slice(ByteBuffer.wrap(key), column_parent, predicate, ConsistencyLevel.ONE);

				final JSONArray fields = new JSONArray();
				for (final ColumnOrSuperColumn result : results) {

					final Column column = result.getColumn();

					final JSONArray field = new JSONArray();
					field.add(new String(column.getName(), "UTF-8"));
					field.add(new String(column.getValue(), "UTF-8"));
					field.add(String.valueOf(column.getTimestamp()));
					fields.add(field);
					logger.trace(new String(column.getName(), "UTF-8") + " = " + new String(column.getValue(), "UTF-8"));
					if (first) {
						first = false;
					}
					else {
						sw.append(",");
					}
					sw.append('"');
					sw.append(new String(key, "UTF-8"));
					sw.append('"');
					sw.append(": ");
					fields.writeJSONString(sw);
					sw.append("\n");
				}
			}
		}
		finally {
			storageConnectionPool.releaseConnection(connection);
		}

		sw.append("}\n");
	}
}
