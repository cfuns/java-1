package de.benjaminborbe.storage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageRowIteratorWhereIntegrationTest {

	private static boolean notFound;

	@BeforeClass
	public static void setUpClass() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConfig config = injector.getInstance(StorageConfig.class);

		final Socket socket = new Socket();
		final SocketAddress endpoint = new InetSocketAddress(config.getHost(), config.getPort());
		try {
			socket.connect(endpoint, 500);

			notFound = !socket.isConnected();
			notFound = false;
		}
		catch (final IOException e) {
			notFound = true;
		}
	}

	@Before
	public void setUp() {
		StorageTestUtil.setUp();
	}

	@After
	public void tearDown() {
		StorageTestUtil.tearDown();
	}

	@Test
	public void testPerformance() throws Exception {
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final StorageConnectionPool storageConnectionPool = injector.getInstance(StorageConnectionPool.class);

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));

		final String encoding = config.getEncoding();
		final StorageValue id = new StorageValue("a", encoding);
		final Map<StorageValue, StorageValue> data = new HashMap<StorageValue, StorageValue>();
		final StorageValue key = new StorageValue(StorageTestUtil.FIELD_NAME, encoding);
		final StorageValue value = new StorageValue("value", encoding);
		data.put(key, value);
		// ein eintrag schreiben
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);

		final List<StorageValue> columnNames = Arrays.asList(new StorageValue(StorageTestUtil.FIELD_NAME, encoding));
		final Map<StorageValue, StorageValue> where = new HashMap<StorageValue, StorageValue>();
		where.put(key, value);

		final StorageRowIteratorWhere i = new StorageRowIteratorWhere(storageConnectionPool, config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, encoding, columnNames, where);
		assertTrue(i.hasNext());
		final StorageRow row = i.next();
		assertEquals(id, row.getKey());
		assertEquals(value, row.getValue(key));
		assertFalse(i.hasNext());
	}
}
