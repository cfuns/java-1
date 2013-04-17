package de.benjaminborbe.storage.util;

import de.benjaminborbe.storage.api.StorageValue;
import org.apache.cassandra.thrift.Cassandra.Client;
import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnOrSuperColumn;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ConsistencyLevel;
import org.apache.cassandra.thrift.SlicePredicate;
import org.easymock.EasyMock;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class StorageColumnIteratorImplUnitTest {

	private static final String keySpace = "keyspace";

	private static final String columnFamily = "columnfamily";

	private static final String encoding = "UTF8";

	private static final StorageValue id = new StorageValue("1337", encoding);

	private static final boolean reversed = false;

	private List<ColumnOrSuperColumn> buildColumn(final int size) throws UnsupportedEncodingException {
		final List<ColumnOrSuperColumn> result = new ArrayList<>();
		for (int i = 0; i < size; ++i) {
			final byte[] name = ("columnName" + i).getBytes(encoding);
			final byte[] value = ("columnValue" + i).getBytes(encoding);

			final Column column = EasyMock.createMock(Column.class);
			EasyMock.expect(column.getName()).andReturn(name).anyTimes();
			EasyMock.expect(column.getValue()).andReturn(value).anyTimes();
			EasyMock.replay(column);

			final ColumnOrSuperColumn e = EasyMock.createMock(ColumnOrSuperColumn.class);
			EasyMock.expect(e.getColumn()).andReturn(column).anyTimes();
			EasyMock.replay(e);

			result.add(e);
		}
		return result;
	}

	@Test
	public void testIteratorEmpty() throws Exception {
		final Client client = EasyMock.createMock(Client.class);
		EasyMock.expect(
			client.get_slice(EasyMock.anyObject(ByteBuffer.class), EasyMock.anyObject(ColumnParent.class), EasyMock.anyObject(SlicePredicate.class),
				EasyMock.anyObject(ConsistencyLevel.class))).andReturn(buildColumn(0));
		EasyMock.replay(client);

		final StorageConnection connection = EasyMock.createMock(StorageConnection.class);
		EasyMock.expect(connection.getClient(keySpace)).andReturn(client);
		EasyMock.replay(connection);

		final StorageConnectionPool storageConnectionPool = EasyMock.createMock(StorageConnectionPool.class);
		EasyMock.expect(storageConnectionPool.getConnection()).andReturn(connection);
		storageConnectionPool.releaseConnection(connection);
		EasyMock.replay(storageConnectionPool);

		final StorageColumnIteratorImpl i = new StorageColumnIteratorImpl(storageConnectionPool, keySpace, columnFamily, encoding, id, reversed);
		assertThat(i.hasNext(), is(false));
		assertThat(i.hasNext(), is(false));
	}

	@Test
	public void testIteratorOne() throws Exception {
		final Client client = EasyMock.createMock(Client.class);
		EasyMock.expect(
			client.get_slice(EasyMock.anyObject(ByteBuffer.class), EasyMock.anyObject(ColumnParent.class), EasyMock.anyObject(SlicePredicate.class),
				EasyMock.anyObject(ConsistencyLevel.class))).andReturn(buildColumn(1));
		EasyMock.expect(
			client.get_slice(EasyMock.anyObject(ByteBuffer.class), EasyMock.anyObject(ColumnParent.class), EasyMock.anyObject(SlicePredicate.class),
				EasyMock.anyObject(ConsistencyLevel.class))).andReturn(buildColumn(0));
		EasyMock.replay(client);

		final StorageConnection connection = EasyMock.createMock(StorageConnection.class);
		EasyMock.expect(connection.getClient(keySpace)).andReturn(client).anyTimes();
		EasyMock.replay(connection);

		final StorageConnectionPool storageConnectionPool = EasyMock.createMock(StorageConnectionPool.class);
		EasyMock.expect(storageConnectionPool.getConnection()).andReturn(connection).anyTimes();
		storageConnectionPool.releaseConnection(connection);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(storageConnectionPool);

		final StorageColumnIteratorImpl i = new StorageColumnIteratorImpl(storageConnectionPool, keySpace, columnFamily, encoding, id, reversed);
		assertThat(i.hasNext(), is(true));
		assertThat(i.hasNext(), is(true));

		assertThat(i.next(), is(not(nullValue())));

		assertThat(i.hasNext(), is(false));
		assertThat(i.hasNext(), is(false));
	}

	@Test
	public void testIteratorTwo() throws Exception {
		final Client client = EasyMock.createMock(Client.class);
		EasyMock.expect(
			client.get_slice(EasyMock.anyObject(ByteBuffer.class), EasyMock.anyObject(ColumnParent.class), EasyMock.anyObject(SlicePredicate.class),
				EasyMock.anyObject(ConsistencyLevel.class))).andReturn(buildColumn(2));
		EasyMock.expect(
			client.get_slice(EasyMock.anyObject(ByteBuffer.class), EasyMock.anyObject(ColumnParent.class), EasyMock.anyObject(SlicePredicate.class),
				EasyMock.anyObject(ConsistencyLevel.class))).andReturn(buildColumn(0));
		EasyMock.replay(client);

		final StorageConnection connection = EasyMock.createMock(StorageConnection.class);
		EasyMock.expect(connection.getClient(keySpace)).andReturn(client).anyTimes();
		EasyMock.replay(connection);

		final StorageConnectionPool storageConnectionPool = EasyMock.createMock(StorageConnectionPool.class);
		EasyMock.expect(storageConnectionPool.getConnection()).andReturn(connection).anyTimes();
		storageConnectionPool.releaseConnection(connection);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.replay(storageConnectionPool);

		final StorageColumnIteratorImpl i = new StorageColumnIteratorImpl(storageConnectionPool, keySpace, columnFamily, encoding, id, reversed);
		assertThat(i.hasNext(), is(true));
		assertThat(i.hasNext(), is(true));

		assertThat(i.next(), is(not(nullValue())));

		assertThat(i.hasNext(), is(true));
		assertThat(i.hasNext(), is(true));

		assertThat(i.next(), is(not(nullValue())));

		assertThat(i.hasNext(), is(false));
		assertThat(i.hasNext(), is(false));
	}

}
