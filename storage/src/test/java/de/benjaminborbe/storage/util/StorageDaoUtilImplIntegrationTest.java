package de.benjaminborbe.storage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.map.MapChain;

public class StorageDaoUtilImplIntegrationTest {

	@Before
	public void setUp() {
		StorageTestUtil.setUp();
	}

	@After
	public void tearDown() {
		StorageTestUtil.tearDown();
	}

	@Test
	public void testCURD() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));

		final String id = "a";
		final Map<String, String> data = new HashMap<String, String>();
		final String key = StorageTestUtil.FIELD_NAME;
		final String value = "valueA\nvalueB";
		data.put(key, value);
		// ein eintrag schreiben
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);

		// eintrag wieder lesen und inhalt vergleich
		assertEquals(value, daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key));

		// ein eitnrag
		assertEquals(1, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, key));

		// eintrag loeschen
		daoUtil.delete(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key);

		// schauen das geloeschter eintrag nicht mehr gelesen werden kann
		assertNull(daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key));

		// nach dem loeschen wieder leer
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, key));
	}

	@Test
	public void testList() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		final List<String> testValues = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9");
		int counter = 0;
		for (final String id : testValues) {
			counter++;

			final Map<String, String> data = new HashMap<String, String>();
			final String key = StorageTestUtil.FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);

			// nach dem loeschen wieder leer
			assertEquals(counter, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));
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

		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));
		for (int id = 1; id <= max; ++id) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = StorageTestUtil.FIELD_NAME;
			data.put(key + "_a", String.valueOf(id));
			data.put(key + "_b", String.valueOf(id));
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, String.valueOf(id), data);
			assertEquals(id, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));
		}
	}

	@Test
	public void testLongList() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		for (int i = 1; i <= 1000; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = StorageTestUtil.FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);
			final String id = "key" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);

			// nach dem loeschen wieder leer
			if (i % 1000 == 0) {
				assertEquals(i, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));
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
		final String id = StorageTestUtil.REPLICATION_FACTOR;
		final String key = StorageTestUtil.FIELD_NAME;

		// insert null
		data.put(StorageTestUtil.FIELD_NAME, null);
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);
		assertNull(daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key));

		// insert a
		data.put(StorageTestUtil.FIELD_NAME, "a");
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);
		assertEquals("a", daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key));

		// insert null
		data.put(StorageTestUtil.FIELD_NAME, null);
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);
		assertNull(daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key));
	}

	@Test
	public void testKeyIterator() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));

		final int limit = 10;
		for (int i = 1; i <= limit; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = StorageTestUtil.FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);
			final String id = "key" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);
		}

		final StorageIterator i = daoUtil.keyIterator(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY);
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
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));

		final int limit = 10;
		for (int i = 1; i <= limit; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = StorageTestUtil.FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);
			final String id = "keyA" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);
		}

		for (int i = 1; i <= limit; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = StorageTestUtil.FIELD_NAME;
			final String value = "valueB";
			data.put(key, value);
			final String id = "keyB" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);
		}

		final StorageIterator i = daoUtil.keyIterator(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, new MapChain<String, String>().add(StorageTestUtil.FIELD_NAME, "valueA"));
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
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key, value);
		assertEquals(value, daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key));
	}
}
