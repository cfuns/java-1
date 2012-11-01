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

public class StorageKeyIterator {

	// COUNT > 1
	private static final int COUNT = 100;

	private final Iface client;

	private final ColumnParent column_parent;

	private final KeyRange range;

	private final SlicePredicate predicate;

	private List<KeySlice> cols;

	private int currentPos;

	public StorageKeyIterator(final Iface client, final ColumnParent column_parent) {
		this.client = client;
		this.column_parent = column_parent;

		final KeyRange range = new KeyRange();
		range.setStart_key(new byte[0]);
		range.setEnd_key(new byte[0]);
		range.setCount(COUNT);
		this.range = range;

		final SlicePredicate predicate = new SlicePredicate();
		predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]), ByteBuffer.wrap(new byte[0]), false, 0));

		this.predicate = predicate;
	}

	public boolean hasNext() throws InvalidRequestException, UnavailableException, TimedOutException, TException {
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

	public byte[] next() throws InvalidRequestException, UnavailableException, TimedOutException, TException {
		if (hasNext()) {
			final byte[] result = cols.get(currentPos).getKey();
			range.setStart_key(result);
			currentPos++;
			return result;
		}
		else {
			throw new NoSuchElementException();
		}
	}

}
