package de.benjaminborbe.storage.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.map.MapChain;

public class StorageDaoUtilImplIntegrationTest {

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
		if (notFound)
			return;
		StorageTestUtil.setUp();
	}

	@After
	public void tearDown() {
		if (notFound)
			return;
		StorageTestUtil.tearDown();
	}

	@Test
	public void testCURD() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));

		final StorageValue id = new StorageValue("a", encoding);
		final Map<StorageValue, StorageValue> data = new HashMap<StorageValue, StorageValue>();
		final StorageValue key = new StorageValue(StorageTestUtil.FIELD_NAME, encoding);
		final StorageValue value = new StorageValue("valueA\nvalueB", encoding);
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
		assertEquals(new StorageValue(), daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key));

		// nach dem loeschen wieder leer
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, key));
	}

	@Test
	public void testList() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		final List<String> testValues = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7",
				"8", "9");
		int counter = 0;
		for (final String idString : testValues) {
			final StorageValue id = new StorageValue(idString, encoding);
			counter++;

			final Map<StorageValue, StorageValue> data = new HashMap<StorageValue, StorageValue>();
			final StorageValue key = new StorageValue(StorageTestUtil.FIELD_NAME, encoding);
			final StorageValue value = new StorageValue("valueA", encoding);
			data.put(key, value);

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);

			// nach dem loeschen wieder leer
			assertEquals(counter, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));
		}
	}

	@Test
	public void testListMultiColumns() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfigMock config = injector.getInstance(StorageConfigMock.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();
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
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(String.valueOf(id), encoding), c(data, encoding));
			assertEquals(id, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));
		}
	}

	@Test
	public void testLongList() throws Exception {
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		for (int i = 1; i <= 1000; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = StorageTestUtil.FIELD_NAME;
			final String value = "valueA";
			data.put(key, value);
			final String id = "key" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));

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
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		// Connection zur Datenbank oeffnen

		final Map<String, String> data = new HashMap<String, String>();
		final String id = StorageTestUtil.REPLICATION_FACTOR;
		final String key = StorageTestUtil.FIELD_NAME;

		// insert null
		data.put(StorageTestUtil.FIELD_NAME, null);
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		assertEquals(c(), daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(key, encoding)));

		// insert a
		data.put(StorageTestUtil.FIELD_NAME, "a");
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		assertEquals(c("a", encoding), daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(key, encoding)));

		// insert null
		data.put(StorageTestUtil.FIELD_NAME, null);
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		assertEquals(c(), daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(key, encoding)));
	}

	private StorageValue c() {
		return new StorageValue();
	}

	@Test
	public void testKeyIterator() throws Exception {
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

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
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		}

		final StorageIterator i = daoUtil.keyIterator(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY);
		assertNotNull(i);
		int counter = 0;
		while (i.hasNext()) {
			counter++;
			final StorageValue key = i.next();
			assertNotNull(key);
			assertTrue(key.getString().indexOf("key") == 0);
		}
		assertEquals(limit, counter);
	}

	@Test
	public void testKeyIteratorWhere() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

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
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		}

		for (int i = 1; i <= limit; ++i) {
			final Map<String, String> data = new HashMap<String, String>();
			final String key = StorageTestUtil.FIELD_NAME;
			final String value = "valueB";
			data.put(key, value);
			final String id = "keyB" + i;

			// ein eintrag schreiben
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		}

		final StorageIterator i = daoUtil.keyIterator(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY,
				new MapChain<StorageValue, StorageValue>().add(c(StorageTestUtil.FIELD_NAME, encoding), c("valueA", encoding)));
		assertNotNull(i);
		int counter = 0;
		while (i.hasNext()) {
			counter++;
			final StorageValue key = i.next();
			assertNotNull(key);
			assertTrue(key.getString().indexOf("keyA") == 0);
		}
		assertEquals(limit, counter);
	}

	@Test
	public void testEncoding() throws Exception {
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();
		final StorageValue id = c("123", encoding);
		final StorageValue key = c("test", encoding);
		final StorageValue value = c("Bäm", encoding);
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key, value);
		assertEquals(value.getString(), daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key).getString());
		assertThat(daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, key), is(value));
	}

	@Test
	public void testDeleteRow() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		final StorageValue key = c("test", encoding);
		final StorageValue value = c("Bäm", encoding);
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), key, value);
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("13", encoding), key, value);
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("14", encoding), key, value);
		assertEquals(value, daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), key));
		assertEquals(value, daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("13", encoding), key));
		assertEquals(value, daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("14", encoding), key));
		daoUtil.delete(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("13", encoding));
		assertEquals(value, daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), key));
		assertEquals(new StorageValue(), daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("13", encoding), key));
		assertEquals(value, daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("14", encoding), key));
	}

	@Test
	public void testReadMultiRow() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("keyA", encoding), c("valueA", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("keyB", encoding), c("valueB", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("keyC", encoding), c("valueC", encoding));

		final List<StorageValue> data = daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding),
				Arrays.asList(c("keyA", encoding), c("keyB", encoding), c("keyC", encoding)));
		assertNotNull(data);
		assertEquals(3, data.size());
		assertEquals(c("valueA", encoding), data.get(0));
		assertEquals(c("valueB", encoding), data.get(1));
		assertEquals(c("valueC", encoding), data.get(2));
	}

	@Test
	public void testReadMultiRowMissingColumns() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("keyA", encoding), c("valueA", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("keyC", encoding), c("valueC", encoding));

		final List<StorageValue> data = daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding),
				Arrays.asList(c("keyA", encoding), c("keyB", encoding), c("keyC", encoding)));
		assertNotNull(data);
		assertEquals(3, data.size());
		assertEquals(c("valueA", encoding), data.get(0));
		assertEquals(c(), data.get(1));
		assertEquals(c("valueC", encoding), data.get(2));
	}

	@Test
	public void testReadRow() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("keyA", encoding), c("valueA", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("keyB", encoding), c("valueB", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("keyC", encoding), c("valueC", encoding));

		final Map<StorageValue, StorageValue> data = daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding));
		assertNotNull(data);
		assertEquals(3, data.size());
		assertEquals(c("valueA", encoding), data.get(c("keyA", encoding)));
		assertEquals(c("valueB", encoding), data.get(c("keyB", encoding)));
		assertEquals(c("valueC", encoding), data.get(c("keyC", encoding)));
	}

	@Test
	public void testReadRowLarge() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		final int count = 1000;
		{
			final Map<String, String> data = new HashMap<String, String>();
			for (int i = 1; i <= count; ++i) {
				data.put("key" + i, "value" + i);
			}
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c(data, encoding));
		}
		final Map<StorageValue, StorageValue> data = daoUtil.read(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding));
		assertNotNull(data);
		assertEquals(count, data.size());
		for (int i = 1; i <= count; ++i) {
			assertEquals(c("value" + i, encoding), data.get(c("key" + i, encoding)));
		}
	}

	@Test
	public void testColumnIterator() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		final int limit = 1000;
		{
			final Map<String, String> data = new HashMap<String, String>();
			for (int i = 1; i <= limit; ++i) {
				data.put("key" + i, "value" + i);
			}
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c(data, encoding));
		}
		int counter = 0;
		final StorageColumnIterator columnIterator = daoUtil.columnIterator(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding));
		while (columnIterator.hasNext()) {
			final StorageColumn column = columnIterator.next();
			counter++;
			assertNotNull(column.getColumnName().getString());
			assertNotNull(column.getColumnValue().getString());
		}
		assertEquals(limit, counter);
	}

	@Test
	public void testColumnIteratorSort() throws Exception {
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("key04", encoding), c("value04", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("key01", encoding), c("value01", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("key11", encoding), c("value11", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("key02", encoding), c("value02", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c("key03", encoding), c("value03", encoding));

		final StorageColumnIterator columnIterator = daoUtil.columnIterator(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding));
		assertTrue(columnIterator.hasNext());
		assertEquals("key01", columnIterator.next().getColumnName().getString());
		assertTrue(columnIterator.hasNext());
		assertEquals("key02", columnIterator.next().getColumnName().getString());
		assertTrue(columnIterator.hasNext());
		assertEquals("key03", columnIterator.next().getColumnName().getString());
		assertTrue(columnIterator.hasNext());
		assertEquals("key04", columnIterator.next().getColumnName().getString());
		assertTrue(columnIterator.hasNext());
		assertEquals("key11", columnIterator.next().getColumnName().getString());
		assertFalse(columnIterator.hasNext());
	}

	@Test
	public void testColumnIteratorByte() throws Exception {
		if (notFound)
			return;

		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final String encoding = config.getEncoding();

		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c(toByteArray(5), encoding), c("a", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c(toByteArray(1), encoding), c("a", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c(toByteArray(11), encoding), c("a", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c(toByteArray(2), encoding), c("a", encoding));
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding), c(toByteArray(3), encoding), c("a", encoding));

		final StorageColumnIterator columnIterator = daoUtil.columnIterator(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c("12", encoding));
		assertTrue(columnIterator.hasNext());
		assertEquals(c(toByteArray(1), encoding), columnIterator.next().getColumnName());
		assertTrue(columnIterator.hasNext());
		assertEquals(c(toByteArray(2), encoding), columnIterator.next().getColumnName());
		assertTrue(columnIterator.hasNext());
		assertEquals(c(toByteArray(3), encoding), columnIterator.next().getColumnName());
		assertTrue(columnIterator.hasNext());
		assertEquals(c(toByteArray(5), encoding), columnIterator.next().getColumnName());
		assertTrue(columnIterator.hasNext());
		assertEquals(c(toByteArray(11), encoding), columnIterator.next().getColumnName());
		assertFalse(columnIterator.hasNext());
	}

	private StorageValue c(final byte[] byteArray, final String encoding) {
		return new StorageValue(byteArray, encoding);
	}

	private Map<StorageValue, StorageValue> c(final Map<String, String> data, final String encoding) {
		final Map<StorageValue, StorageValue> result = new HashMap<StorageValue, StorageValue>();
		for (final Entry<String, String> e : data.entrySet()) {
			result.put(c(e.getKey(), encoding), c(e.getValue(), encoding));
		}
		return result;
	}

	private StorageValue c(final String value, final String encoding) {
		return new StorageValue(value, encoding);
	}

	private byte[] toByteArray(final int i) {
		return new byte[] { new Integer(i).byteValue() };
	}

}
