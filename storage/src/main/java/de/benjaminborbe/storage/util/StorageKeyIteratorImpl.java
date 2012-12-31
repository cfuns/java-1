package de.benjaminborbe.storage.util;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageValue;

public class StorageKeyIteratorImpl implements StorageIterator {

	// COUNT > 1
	private static final int COUNT = 100;

	private final ColumnParent column_parent;

	private final KeyRange range;

	private final SlicePredicate predicate;

	private List<KeySlice> cols;

	private int currentPos;

	private final String encoding;

	private final StorageConnectionPool storageConnectionPool;

	private final String keySpace;

	public StorageKeyIteratorImpl(final StorageConnectionPool storageConnectionPool, final String keySpace, final String columnFamily, final String encoding) {
		this.storageConnectionPool = storageConnectionPool;
		this.keySpace = keySpace;
		this.column_parent = new ColumnParent(columnFamily);
		this.encoding = encoding;

		final KeyRange range = new KeyRange();
		range.setStart_key(new byte[0]);
		range.setEnd_key(new byte[0]);
		range.setCount(COUNT);
		this.range = range;

		final SlicePredicate predicate = new SlicePredicate();
		predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]), ByteBuffer.wrap(new byte[0]), false, 0));

		this.predicate = predicate;
	}

	@Override
	public boolean hasNext() throws StorageException {

		StorageConnection connection = null;
		try {
			connection = storageConnectionPool.getConnection();
			final Iface client = connection.getClient(keySpace);

			if (cols == null) {
				cols = client.get_range_slices(column_parent, predicate, range, ConsistencyLevel.ONE);
				currentPos = 0;
			}
			else if (currentPos == cols.size()) {
				cols = client.get_range_slices(column_parent, predicate, range, ConsistencyLevel.ONE);
				currentPos = 1;
			}
			return currentPos < cols.size();
		}
		catch (final InvalidRequestException e) {
			throw new StorageException(e);
		}
		catch (final UnavailableException e) {
			throw new StorageException(e);
		}
		catch (final TimedOutException e) {
			throw new StorageException(e);
		}
		catch (final TException e) {
			throw new StorageException(e);
		}
		catch (final StorageConnectionPoolException e) {
			throw new StorageException(e);
		}
		finally {
			storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public StorageValue next() throws StorageException {
		if (hasNext()) {
			final byte[] result = cols.get(currentPos).getKey();
			range.setStart_key(result);
			currentPos++;
			return new StorageValue(result, encoding);
		}
		else {
			throw new NoSuchElementException();
		}
	}

}
