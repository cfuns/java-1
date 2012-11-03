package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.Cassandra.Iface;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.IndexClause;
import org.apache.cassandra.thrift.IndexExpression;
import org.apache.cassandra.thrift.IndexOperator;
import org.apache.cassandra.thrift.InvalidRequestException;
import org.apache.cassandra.thrift.KeyRange;
import org.apache.cassandra.thrift.KeySlice;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SliceRange;
import org.apache.cassandra.thrift.TimedOutException;
import org.apache.cassandra.thrift.UnavailableException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.benjaminborbe.storage.api.StorageException;

public class ListKeysScript {

	private static final int SOCKET_TIMEOUT = 7000;

	private final ThreadLocal<Client> clientThreadLocal = new ThreadLocal<Client>();

	private final ThreadLocal<TFramedTransport> trThreadLocal = new ThreadLocal<TFramedTransport>();

	private final Logger logger = LoggerFactory.getLogger("test");

	private final String host = "localhost";

	private final String keySpace = "bb";

	private final String columnFamily = "user";

	private final int readLimit = 1000;

	private final String encoding = "UTF8";

	private final int port = 9160;

	public static void main(final String[] args) {
		final ListKeysScript listKeysScript = new ListKeysScript();
		try {
			listKeysScript.run();
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void run() throws Exception {
		try {
			open();
			// for (final String key : list()) {
			// System.out.println(key);
			// }

			// System.err.println("-----");

			// list2();
			// list3();
			// list4();
			list5();

		}
		finally {
			close();
		}
	}

	public void list5() throws InvalidRequestException, UnavailableException, TimedOutException, TException, UnsupportedEncodingException {

		final ColumnParent column_parent = new ColumnParent("bookmark");

		final Iface client = getClient(keySpace);

		final KeyRange range = new KeyRange();
		range.setStart_key(new byte[0]);
		range.setEnd_key(new byte[0]);

		final SlicePredicate predicate = new SlicePredicate();
		predicate.setColumn_names(Arrays.asList(buffer("ownerUsername")));
		range.setRow_filter(Arrays.asList(new IndexExpression(buffer("ownerUsername"), IndexOperator.EQ, buffer("bborbe"))));

		List<KeySlice> cols = client.get_range_slices(column_parent, predicate, range, ConsistencyLevel.ONE);
		while (cols.size() > 0) {
			for (int i = 0; i < cols.size(); ++i) {
				System.err.println(new String(cols.get(i).getKey()));
			}
			System.err.println("cols.size=" + cols.size());
			final KeySlice b = cols.get(cols.size() - 1);
			range.setStart_key(b.key);
			cols = client.get_range_slices(column_parent, predicate, range, ConsistencyLevel.ONE);
			if (cols.size() == 1)
				return;
		}

	}

	public void list4() throws InvalidRequestException, TException, UnavailableException, TimedOutException, UnsupportedEncodingException {
		final ColumnParent column_parent = new ColumnParent("bookmark");

		final Iface client = getClient(keySpace);

		final SlicePredicate column_predicate = new SlicePredicate();
		column_predicate.setColumn_names(Arrays.asList(buffer("ownerUsername")));

		final IndexClause index_clause = new IndexClause();
		index_clause.setCount(5);
		index_clause.setStart_key(new byte[0]);
		index_clause.setExpressions(Arrays.asList(new IndexExpression(buffer("ownerUsername"), IndexOperator.EQ, buffer("bborbe"))));

		List<KeySlice> cols = client.get_indexed_slices(column_parent, index_clause, column_predicate, ConsistencyLevel.ONE);
		while (cols.size() > 0) {
			for (int i = 0; i < cols.size(); ++i) {
				System.err.println(new String(cols.get(i).getKey()));
			}
			System.err.println("cols.size=" + cols.size());
			final KeySlice b = cols.get(cols.size() - 1);
			index_clause.setStart_key(b.key);
			cols = client.get_indexed_slices(column_parent, index_clause, column_predicate, ConsistencyLevel.ONE);
			if (cols.size() == 1)
				return;
		}
	}

	private ByteBuffer buffer(final String string) throws UnsupportedEncodingException {
		return ByteBuffer.wrap(string.getBytes("UTF-8"));
	}

	public void list3() throws StorageException, InvalidRequestException, TException {
		final ColumnParent column_parent = new ColumnParent("user");
		final Iface client = getClient(keySpace);
		final StorageKeyIterator i = new StorageKeyIterator(client, column_parent);
		while (i.hasNext()) {
			System.out.println(new String(i.next()));
		}
	}

	public void list2() throws InvalidRequestException, UnavailableException, TimedOutException, TException {

		final ColumnParent column_parent = new ColumnParent("worktime");

		final Iface client = getClient(keySpace);

		final KeyRange range = new KeyRange();
		range.setStart_key(new byte[0]);
		range.setEnd_key(new byte[0]);

		final SlicePredicate predicate = new SlicePredicate();
		predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]), ByteBuffer.wrap(new byte[0]), false, 0));

		List<KeySlice> cols = client.get_range_slices(column_parent, predicate, range, ConsistencyLevel.ONE);
		while (cols.size() > 0) {
			for (int i = 0; i < cols.size(); ++i) {
				System.err.println(new String(cols.get(i).getKey()));
			}
			System.err.println("cols.size=" + cols.size());
			final KeySlice b = cols.get(cols.size() - 1);
			range.setStart_key(b.key);
			cols = client.get_range_slices(column_parent, predicate, range, ConsistencyLevel.ONE);
			if (cols.size() == 1)
				return;
		}

	}

	public List<String> list() throws UnsupportedEncodingException, InvalidRequestException, TException, UnavailableException, TimedOutException {
		final Iface client = getClient(keySpace);
		logger.trace("list keyspace: " + keySpace + " columnfamily: " + columnFamily + " readlimit = " + readLimit);

		final Set<String> result = new HashSet<String>();
		final ColumnParent column_parent = new ColumnParent(columnFamily);
		final SlicePredicate predicate = new SlicePredicate();
		final int columnCount = 1;
		predicate.setSlice_range(new SliceRange(ByteBuffer.wrap(new byte[0]), ByteBuffer.wrap(new byte[0]), false, columnCount));

		byte[] startKey = null;
		final byte[] endKey = null;
		while (true) {
			logger.trace("startKey " + (startKey != null ? new String(startKey, encoding) : "null"));
			final KeyRange keyRange = new KeyRange(readLimit + 1);
			keyRange.setStart_key(startKey != null ? startKey : new byte[0]);
			keyRange.setEnd_key(endKey != null ? endKey : new byte[0]);
			final ConsistencyLevel consistency_level = ConsistencyLevel.ONE;
			final List<KeySlice> keySlices = filterWithColumns(client.get_range_slices(column_parent, predicate, keyRange, consistency_level));
			if (keySlices.isEmpty()) {
				logger.trace("found " + result.size() + " elements in keyspace: " + keySpace + " columnfamily: " + columnFamily);
				return new ArrayList<String>(result);
			}
			for (int i = 0; i < keySlices.size(); ++i) {
				final KeySlice keySlice = keySlices.get(i);
				final String key = new String(keySlice.getKey(), encoding);
				if (i != 0 && result.contains(key) || keySlices.size() == 1) {
					logger.trace("found " + result.size() + " elements in keyspace: " + keySpace + " columnfamily: " + columnFamily);
					result.add(key);
					return new ArrayList<String>(result);
				}
				result.add(key);
				startKey = keySlice.getKey();
			}
		}
	}

	protected Iface getClient(final String keySpace) throws InvalidRequestException, TException {
		final Client client = clientThreadLocal.get();
		client.set_keyspace(keySpace);
		return client;
	}

	private List<KeySlice> filterWithColumns(final List<KeySlice> keySlices) {
		final List<KeySlice> result = new ArrayList<KeySlice>();
		for (final KeySlice k : keySlices) {
			if (k.getColumnsSize() > 0)
				result.add(k);
		}
		return result;
	}

	public void open() throws TTransportException, SocketException {

		final TSocket socket = new TSocket(host, port);
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

	public void close() {
		logger.trace("close cassandra connection");
		final TFramedTransport tr = trThreadLocal.get();
		trThreadLocal.remove();
		clientThreadLocal.remove();
		if (tr != null) {
			try {
				tr.flush();
				tr.close();
			}
			catch (final TTransportException e) {
			}
		}
	}
}
