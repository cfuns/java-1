package de.benjaminborbe.storage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.cassandra.thrift.CfDef;
import org.apache.cassandra.thrift.KsDef;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageDaoUtilIntegrationTest {

	private static final String FIELD_NAME = "keyA";

	private static final String COLUMNFAMILY = "test";

	@Before
	public void setUp() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConnection connection = injector.getInstance(StorageConnection.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		try {
			// Connection zur Datenbank oeffnen
			connection.open();

			// Definition ders KeySpaces
			final List<CfDef> cfDefList = new ArrayList<CfDef>();
			final CfDef input = new CfDef(config.getKeySpace(), COLUMNFAMILY);
			input.setComparator_type("AsciiType");
			input.setDefault_validation_class("AsciiType");
			cfDefList.add(input);

			// Erstellt einen neuen KeySpace
			final int replication_factor = 1;
			connection.getClient().system_add_keyspace(new KsDef(config.getKeySpace(), org.apache.cassandra.locator.SimpleStrategy.class.getName(), replication_factor, cfDefList));
			final int magnitude = connection.getClient().describe_ring(config.getKeySpace()).size();
			try {
				Thread.sleep(1000 * magnitude);
			}
			catch (final InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		catch (final Exception e) {
			e.printStackTrace();
		}
		finally {
			// Connection zur Datenbank wieder schliessen
			connection.close();
		}
	}

	@After
	public void tearDown() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConnection connection = injector.getInstance(StorageConnection.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		try {
			// Connection zur Datenbank oeffnen
			connection.open();

			for (final String key : daoUtil.list(config.getKeySpace(), COLUMNFAMILY)) {
				daoUtil.delete(config.getKeySpace(), COLUMNFAMILY, key, FIELD_NAME);
			}

			// KeySpace loeschen
			connection.getClient().system_drop_column_family(COLUMNFAMILY);

			// KeySpace loeschen
			connection.getClient().system_drop_keyspace(config.getKeySpace());
		}
		catch (final Exception e) {

		}
		finally {
			// Connection zur Datenbank wieder schliessen
			connection.close();
		}

	}

	@Test
	public void testCURD() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConnection connection = injector.getInstance(StorageConnection.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		try {
			// Connection zur Datenbank oeffnen
			connection.open();

			// leer db
			assertEquals(0, daoUtil.list(config.getKeySpace(), COLUMNFAMILY).size());

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
			assertEquals(1, daoUtil.list(config.getKeySpace(), COLUMNFAMILY).size());

			// eintrag loeschen
			daoUtil.delete(config.getKeySpace(), COLUMNFAMILY, id, key);

			// schauen das geloeschter eintrag nicht mehr gelesen werden kann
			assertNull(daoUtil.read(config.getKeySpace(), COLUMNFAMILY, id, key));

			// nach dem loeschen wieder leer
			assertEquals(0, daoUtil.list(config.getKeySpace(), COLUMNFAMILY).size());

		}
		finally {
			// Connection zur Datenbank wieder schliessen
			connection.close();
		}
	}

	@Test
	public void testList() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConnection connection = injector.getInstance(StorageConnection.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		try {
			// Connection zur Datenbank oeffnen
			connection.open();

			final List<String> testValues = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
					"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
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
				assertEquals(counter, daoUtil.list(config.getKeySpace(), COLUMNFAMILY).size());

			}

		}
		finally {
			// Connection zur Datenbank wieder schliessen
			connection.close();
		}
	}

	@Test
	public void testLongList() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConnection connection = injector.getInstance(StorageConnection.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		try {
			// Connection zur Datenbank oeffnen
			connection.open();

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
					assertEquals(i, daoUtil.list(config.getKeySpace(), COLUMNFAMILY).size());
				}
			}
		}
		finally {
			// Connection zur Datenbank wieder schliessen
			connection.close();
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

		final StorageConnection connection = injector.getInstance(StorageConnection.class);
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);

		try {
			// Connection zur Datenbank oeffnen
			connection.open();

			final Map<String, String> data = new HashMap<String, String>();
			final String id = "1";
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
		finally {
			// Connection zur Datenbank wieder schliessen
			connection.close();
		}
	}
}
