package de.benjaminborbe.storage.util;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageValue;
import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.IndexClause;
import org.apache.cassandra.thrift.IndexExpression;
import org.apache.cassandra.thrift.IndexOperator;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class StorageRowIteratorWhere implements StorageRowIterator {

	// COUNT > 1
	private static final int COUNT = 100;

	private final ColumnParent column_parent;

	private final IndexClause index_clause;

	private final SlicePredicate predicate;

	private List<KeySlice> cols;

	private int currentPos;

	private final String encoding;

	private final StorageConnectionPool storageConnectionPool;

	private final String keySpace;

	public StorageRowIteratorWhere(
		final StorageConnectionPool storageConnectionPool,
		final String keySpace,
		final String columnFamily,
		final String encoding,
		final List<StorageValue> columnNames,
		final Map<StorageValue, StorageValue> where
	) throws UnsupportedEncodingException {
		this.storageConnectionPool = storageConnectionPool;
		this.keySpace = keySpace;
		this.column_parent = new ColumnParent(columnFamily);
		this.encoding = encoding;

		index_clause = new IndexClause();
		for (final Entry<StorageValue, StorageValue> e : where.entrySet()) {

			final ByteBuffer column_name = ByteBuffer.wrap(e.getKey().getByte());
			final IndexOperator op = IndexOperator.EQ;
			final ByteBuffer value = ByteBuffer.wrap(e.getValue().getByte());
			final IndexExpression indexExpression = new IndexExpression(column_name, op, value);
			index_clause.addToExpressions(indexExpression);
		}

		index_clause.setStart_key(new byte[0]);
		index_clause.setCount(COUNT);

		final SlicePredicate predicate = new SlicePredicate();
		predicate.setColumn_names(buildColumnNames(columnNames));

		this.predicate = predicate;
	}

	@Override
	public boolean hasNext() throws StorageException {

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			if (cols == null) {
				cols = client.get_indexed_slices(column_parent, index_clause, predicate, ConsistencyLevel.ONE);
				currentPos = 0;
			} else if (currentPos == cols.size()) {
				cols = client.get_indexed_slices(column_parent, index_clause, predicate, ConsistencyLevel.ONE);
				currentPos = 1;
			}
			return currentPos < cols.size();
		} catch (final UnavailableException e) {
			throw new StorageException(e);
		} catch (InvalidRequestException e) {
			throw new StorageException(e);
		} catch (StorageConnectionPoolException e) {
			throw new StorageException(e);
		} catch (TException e) {
			throw new StorageException(e);
		} finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	private List<ByteBuffer> buildColumnNames(final List<StorageValue> columnNames) throws UnsupportedEncodingException {
		final List<ByteBuffer> result = new ArrayList<ByteBuffer>();
		for (final StorageValue columnName : columnNames) {
			result.add(ByteBuffer.wrap(columnName.getByte()));
		}
		return result;
	}

	@Override
	public StorageRow next() throws StorageException {
		try {
			if (hasNext()) {
				final KeySlice keySlice = cols.get(currentPos);
				index_clause.setStart_key(keySlice.getKey());
				final Map<StorageValue, StorageValue> data = new HashMap<StorageValue, StorageValue>();
				for (final ColumnOrSuperColumn c : keySlice.getColumns()) {
					final Column column = c.getColumn();
					data.put(new StorageValue(column.getName(), encoding), new StorageValue(column.getValue(), encoding));
				}
				currentPos++;
				return new StorageRowImpl(new StorageValue(keySlice.getKey(), encoding), data);
			} else {
				throw new NoSuchElementException();
			}
		} finally {
		}
	}
}
