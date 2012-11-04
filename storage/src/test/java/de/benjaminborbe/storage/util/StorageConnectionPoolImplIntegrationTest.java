package de.benjaminborbe.storage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageConnectionPoolImplIntegrationTest {

	@Test
	public void testIsSingleton() {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConnectionPool o1 = injector.getInstance(StorageConnectionPool.class);
		final StorageConnectionPool o2 = injector.getInstance(StorageConnectionPool.class);
		assertEquals(o1, o2);
		assertTrue(o1 == o2);
		assertEquals(o1.getClass(), o2.getClass());
	}

	@Test
	public void testGetConnection() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConnectionPool connectionPool = injector.getInstance(StorageConnectionPool.class);
		StorageConnection connection = null;
		try {
			connection = connectionPool.getConnection();
			assertNotNull(connection);
			assertNotNull(connection.getClient());
			assertNotNull(connection.getTr());
			assertTrue(connection.getTr().isOpen());
		}
		finally {
			connectionPool.close();
			assertFalse(connection.getTr().isOpen());
		}
	}

	@Test
	public void testPooling() throws Exception {
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConnectionPool connectionPool = injector.getInstance(StorageConnectionPool.class);
		final List<StorageConnection> connections = new ArrayList<StorageConnection>();
		try {
			assertEquals(0, connectionPool.getConnections());
			assertEquals(0, connectionPool.getFreeConnections());
			assertEquals(5, connectionPool.getMaxConnections());

			for (int i = 1; i <= 5; ++i) {
				final StorageConnection connection = connectionPool.getConnection();
				assertNotNull(connection);
				assertNotNull(connection.getClient());
				assertNotNull(connection.getTr());
				assertTrue(connection.getTr().isOpen());
				connections.add(connection);

				assertEquals(i, connectionPool.getConnections());
				assertEquals(0, connectionPool.getFreeConnections());
				assertEquals(5, connectionPool.getMaxConnections());
			}

			assertEquals(5, connectionPool.getConnections());
			assertEquals(0, connectionPool.getFreeConnections());
			assertEquals(5, connectionPool.getMaxConnections());

			for (int i = 0; i < 5; ++i) {
				connectionPool.releaseConnection(connections.get(0));
				assertEquals(5, connectionPool.getConnections());
				assertEquals(i + 1, connectionPool.getFreeConnections());
				assertEquals(5, connectionPool.getMaxConnections());
			}

			assertEquals(5, connectionPool.getConnections());
			assertEquals(5, connectionPool.getFreeConnections());
			assertEquals(5, connectionPool.getMaxConnections());

			for (int i = 1; i <= 5; ++i) {
				final StorageConnection connection = connectionPool.getConnection();
				assertNotNull(connection);
				assertNotNull(connection.getClient());
				assertNotNull(connection.getTr());
				assertTrue(connection.getTr().isOpen());
				connections.add(connection);

				assertEquals(5, connectionPool.getConnections());
				assertEquals(5 - i, connectionPool.getFreeConnections());
				assertEquals(5, connectionPool.getMaxConnections());
			}

			assertEquals(10, connections.size());
		}
		finally {
			connectionPool.close();
			assertEquals(0, connectionPool.getConnections());
			assertEquals(0, connectionPool.getFreeConnections());
			assertEquals(5, connectionPool.getMaxConnections());

			for (final StorageConnection connection : connections) {
				assertFalse(connection.getTr().isOpen());
			}
		}
	}
}
