package de.benjaminborbe.storage.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import de.benjaminborbe.tools.json.JSONObject;
import de.benjaminborbe.tools.json.JSONParser;
import de.benjaminborbe.tools.json.JSONParserSimple;
import de.benjaminborbe.tools.util.IdGeneratorUUID;

public class StorageExporterIntegrationTest {

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
	public void testExport() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final IdGeneratorUUID idGeneratorUUID = injector.getInstance(IdGeneratorUUID.class);
		final StorageExporter exporter = injector.getInstance(StorageExporter.class);
		final String encoding = config.getEncoding();

		final StringWriter sw = new StringWriter();
		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));

		{
			final String id = idGeneratorUUID.nextId();
			final Map<String, String> data = new HashMap<String, String>();
			data.put("a", "a");
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		}
		{
			final String id = idGeneratorUUID.nextId();
			final Map<String, String> data = new HashMap<String, String>();
			data.put("a", "b");
			data.put("b", "b");
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		}
		{
			final String id = idGeneratorUUID.nextId();
			final Map<String, String> data = new HashMap<String, String>();
			data.put("c", "c");
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		}
		{
			final String id = idGeneratorUUID.nextId();
			final Map<String, String> data = new HashMap<String, String>();
			data.put("c", "'");
			data.put("d", "\"");
			daoUtil.insert(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, c(id, encoding), c(data, encoding));
		}
		exporter.export(sw, config.getKeySpace(), StorageTestUtil.COLUMNFAMILY);

		final String jsonString = sw.toString();
		assertNotNull(jsonString);
		final JSONParser parser = new JSONParserSimple();
		final Object object = parser.parse(jsonString);
		assertTrue(object instanceof JSONObject);
		final JSONObject json = (JSONObject) object;
		assertEquals(4, json.size());
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

}
