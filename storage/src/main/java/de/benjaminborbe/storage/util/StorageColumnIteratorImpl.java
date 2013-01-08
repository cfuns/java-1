package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;

import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageValue;

public class StorageColumnIteratorImpl implements StorageColumnIterator {

	private final StorageConnectionPool storageConnectionPool;

	private final String keySpace;

	private final String encoding;

	private final ByteBuffer key;

	private final ColumnParent columnParent;

	private final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;

	private List<ColumnOrSuperColumn> columns = null;

	private final SlicePredicate predicate;

	private final SliceRange slice_range;

	private final int count = 100;

	private int pos = 0;

	public StorageColumnIteratorImpl(final StorageConnectionPool storageConnectionPool, final String keySpace, final String columnFamily, final String encoding, final StorageValue id)
			throws UnsupportedEncodingException {
		this.storageConnectionPool = storageConnectionPool;
		this.keySpace = keySpace;
		this.encoding = encoding;
		this.key = ByteBuffer.wrap(id.getByte());
		this.columnParent = new ColumnParent(columnFamily);
		this.predicate = new SlicePredicate();
		this.slice_range = new SliceRange();
		this.predicate.setSlice_range(slice_range);
		this.slice_range.setCount(count);
		this.slice_range.setStart(new byte[0]);
		this.slice_range.setFinish(new byte[0]);
	}

	@Override
	public boolean hasNext() throws StorageException {
		StorageConnection connection = null;
		try {
			if (columns == null || columns.size() == pos) {
				connection = storageConnectionPool.getConnection();
				final Iface client = connection.getClient(keySpace);

				if (columns != null) {
					slice_range.setStart(columns.get(columns.size() - 1).getColumn().getName());
					pos = 1;
				}
				else {
					pos = 0;
				}
				columns = client.get_slice(key, columnParent, predicate, consistency_level);
			}
			return columns != null && columns.size() > pos;
		}
		catch (final StorageConnectionPoolException e) {
			throw new StorageException(e);
		}
		catch (final InvalidRequestException e) {
			throw new StorageException(e);
		}
		catch (final TException e) {
			throw new StorageException(e);
		}
		catch (final UnavailableException e) {
			throw new StorageException(e);
		}
		catch (final TimedOutException e) {
			throw new StorageException(e);
		}
		finally {
			if (connection != null)
				storageConnectionPool.releaseConnection(connection);
		}
	}

	@Override
	public StorageColumn next() throws StorageException {
		if (hasNext()) {
			final ColumnOrSuperColumn column = columns.get(pos);
			final StorageColumn result = new StorageColumnImpl(new StorageValue(column.getColumn().getName(), encoding), new StorageValue(column.getColumn().getValue(), encoding));
			pos++;
			return result;
		}
		else {
			throw new NoSuchElementException();
		}
	}

}
