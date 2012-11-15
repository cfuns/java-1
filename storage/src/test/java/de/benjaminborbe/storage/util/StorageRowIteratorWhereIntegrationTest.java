package de.benjaminborbe.storage.util;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageRowIteratorWhereIntegrationTest {

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
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());

		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final StorageConnectionPool storageConnectionPool = injector.getInstance(StorageConnectionPool.class);

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));

		final String id = "a";
		final Map<String, String> data = new HashMap<String, String>();
		final String key = StorageTestUtil.FIELD_NAME;
		final String value = "value";
		data.put(key, value);
		// ein eintrag schreiben
		daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, id, data);

		final List<String> columnNames = Arrays.asList(StorageTestUtil.FIELD_NAME);
		final Map<String, String> where = new HashMap<String, String>();
		where.put(key, value);

		final StorageRowIteratorWhere i = new StorageRowIteratorWhere(storageConnectionPool, config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, config.getEncoding(), columnNames, where);
		assertTrue(i.hasNext());
		final StorageRow row = i.next();
		assertEquals(id, row.getKeyString());
		assertEquals(value, row.getString(key));
		assertFalse(i.hasNext());
	}
}
