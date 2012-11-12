package de.benjaminborbe.storage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.locator.NetworkTopologyStrategy;
import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.ColumnDef;
import org.apache.cassandra.thrift.IndexType;
import org.apache.cassandra.thrift.KsDef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.map.MapChain;

public class StorageDaoUtilImplIntegrationTest {

	private static final String REPLICATION_FACTOR = "1";

	// private static final String TYPE = "AsciiType";

	private static final String TYPE = "UTF8Type";

	private static final String FIELD_NAME = "indexedColumn";

	private static final String COLUMNFAMILY = "test";

	private static final String ENCODING = "UTF8";

	@Before
	public void setUp() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final Logger logger = injector.getInstance(Logger.class);
		final StorageConnectionPool connectionPool = injector.getInstance(StorageConnectionPool.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		StorageConnection connection = null;
		try {
			connection = connectionPool.getConnection();

			try {
				logger.debug("drop keyspace");
				connection.getClient().system_drop_keyspace(config.getKeySpace());
			}
			catch (final Exception e1) {
			}

			// Definition ders KeySpaces
			// Create CF
			final CfDef input = new CfDef(config.getKeySpace(), COLUMNFAMILY);
			input.setComparator_type(TYPE);
			input.setDefault_validation_class(TYPE);
			input.setKey_validation_class(TYPE);

			{
				final ColumnDef column = new ColumnDef();
				column.setName(FIELD_NAME.getBytes(ENCODING));
				column.setValidation_class(TYPE);
				// column.setIndex_name(FIELD_NAME + "_index");
				column.setIndex_type(IndexType.KEYS);
				input.addToColumn_metadata(column);
			}

			// Erstellt einen neuen KeySpace
			final List<CfDef> cfDefList = new ArrayList<CfDef>();
			cfDefList.add(input);
			final KsDef ksdef = new KsDef(config.getKeySpace(), NetworkTopologyStrategy.class.getName(), cfDefList);
			final Map<String, String> strategy_options = new HashMap<String, String>();
			// strategy_options.put("replication_factor", REPLICATION_FACTOR);
			strategy_options.put("datacenter1", "1");
			ksdef.setStrategy_options(strategy_options);
			ksdef.setDurable_writes(true);
			connection.getClient().system_add_keyspace(ksdef);

			final int magnitude = connection.getClient().describe_ring(config.getKeySpace()).size();
			try {
				Thread.sleep(1000 * magnitude);
			}
			catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}

		}
		catch (final Exception e) {
			logger.debug(e.getClass().getName(), e);
			try {
				logger.debug("drop keyspace");
				connection.getClient().system_drop_keyspace(config.getKeySpace());
			}
			catch (final Exception e1) {
			}
		}
		finally {
			connectionPool.close();
		}
	}

	@After
	public void tearDown() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final Logger logger = injector.getInstance(Logger.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageConnectionPool connectionPool = injector.getInstance(StorageConnectionPool.class);

		StorageConnection connection = null;
		try {
			connection = connectionPool.getConnection();
			logger.debug("drop keyspace");
			connection.getClient().system_drop_keyspace(config.getKeySpace());
		}
		catch (final Exception e) {
		}

		finally {
			connectionPool.close();
		}
	}

	@Test
	public void testCURD() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), COLUMNFAMILY));

		final String id = "a";
		final Map<String, String> data = new HashMap<String, String>();
		final String key = FIELD_NAME;
		final String value = "valueA\nvalueB";
		data.put(key, value);
		// ein eintrag schreiben
		daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);

		// eintrag wieder lesen und inhalt vergleich
		assertEquals(value, daoUtil.read(config.getKeySpace(), COLUMNFAMILY, id, key));

		// ein eitnrag
		assertEquals(1, daoUtil.count(config.getKeySpace(), COLUMNFAMILY, key));

		// eintrag loeschen
		daoUtil.delete(config.getKeySpace(), COLUMNFAMILY, id, key);

		// schauen das geloeschter eintrag nicht mehr gelesen werden kann
		assertNull(daoUtil.read(config.getKeySpace(), COLUMNFAMILY, id, key));

		// nach dem loeschen wieder leer
		assertEquals(0, daoUtil.count(config.getKeySpace(), COLUMNFAMILY, key));
	}

	@Test
	public void testList() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		final List<String> testValues = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", REPLICATION_FACTOR, "2", "3", "4",
				"5", "6", "7", "8", "9");
		int counter = 0;
		for (final String id : testValues) {
			counter++;

			final Map<String, String> data = new HashMap<String, String>();
			final String key = FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);

			// nach dem loeschen wieder leer
			assertEquals(counter, daoUtil.count(config.getKeySpace(), COLUMNFAMILY));
		}
	}

	@Test
	public void testListMultiColumns() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfigMock config = injector.getInstance(StorageConfigMock.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final int limit = 10;
		final int max = 100;
		config.setReadLimit(limit);
		assertEquals(limit, config.getReadLimit());

		assertEquals(0, daoUtil.count(config.getKeySpace(), COLUMNFAMILY));
		for (int id = 1; id <= max; ++id) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = FIELD_NAME;
			data.put(key + "_a", String.valueOf(id));
			data.put(key + "_b", String.valueOf(id));
			daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, String.valueOf(id), data);
			assertEquals(id, daoUtil.count(config.getKeySpace(), COLUMNFAMILY));
		}
	}

	@Test
	public void testLongList() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		for (int i = 1; i <= 1000; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);
			final String id = "key" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);

			// nach dem loeschen wieder leer
			if (i % 1000 == 0) {
				assertEquals(i, daoUtil.count(config.getKeySpace(), COLUMNFAMILY));
			}
		}
	}

	protected String generateLongString(final int length) {
		final StringBuffer result = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			result.append('x');
		}
		return result.toString();
	}

	@Test
	public void testNullValue() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		// Connection zur Datenbank oeffnen

		final Map<String, String> data = new HashMap<String, String>();
		final String id = REPLICATION_FACTOR;
		final String key = FIELD_NAME;

		// insert null
		data.put(FIELD_NAME, null);
		daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);
		assertNull(daoUtil.read(config.getKeySpace(), COLUMNFAMILY, id, key));

		// insert a
		data.put(FIELD_NAME, "a");
		daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);
		assertEquals("a", daoUtil.read(config.getKeySpace(), COLUMNFAMILY, id, key));

		// insert null
		data.put(FIELD_NAME, null);
		daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);
		assertNull(daoUtil.read(config.getKeySpace(), COLUMNFAMILY, id, key));
	}

	@Test
	public void testKeyIterator() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), COLUMNFAMILY));

		final int limit = 10;
		for (int i = 1; i <= limit; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);
			final String id = "key" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);
		}

		final StorageIterator i = daoUtil.keyIterator(config.getKeySpace(), COLUMNFAMILY);
		assertNotNull(i);
		int counter = 0;
		while (i.hasNext()) {
			counter++;
			final String key = i.nextString();
			assertNotNull(key);
			assertTrue(key.indexOf("key") == 0);
		}
		assertEquals(limit, counter);
	}

	@Test
	public void testKeyIteratorWhere() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), COLUMNFAMILY));

		final int limit = 10;
		for (int i = 1; i <= limit; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);
			final String id = "keyA" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);
		}

		for (int i = 1; i <= limit; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = FIELD_NAME;
			final String value = "valueB";
			data.put(key, value);
			final String id = "keyB" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, data);
		}

		final StorageIterator i = daoUtil.keyIterator(config.getKeySpace(), COLUMNFAMILY, new MapChain<String, String>().add(FIELD_NAME, "valueA"));
		assertNotNull(i);
		int counter = 0;
		while (i.hasNext()) {
			counter++;
			final String key = i.nextString();
			assertNotNull(key);
			assertTrue(key.indexOf("keyA") == 0);
		}
		assertEquals(limit, counter);
	}

	@Test
	public void testEncoding() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		final String id = "123";
		final String key = "test";
		final String value = "Bäm";
		daoUtil.insert(config.getKeySpace(), COLUMNFAMILY, id, key, value);
		assertEquals(value, daoUtil.read(config.getKeySpace(), COLUMNFAMILY, id, key));
	}
}
