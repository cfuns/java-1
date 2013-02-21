package de.benjaminborbe.storage.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageImporterIntegrationTest {

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
		finally {
			try {
				socket.close();
			}
			catch (final IOException e) {
			}
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
	public void testImport() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final StorageConfig config = injector.getInstance(StorageConfig.class);
		final StorageDaoUtil daoUtil = injector.getInstance(StorageDaoUtil.class);
		final StorageImporter importer = injector.getInstance(StorageImporter.class);

		// leer db
		assertEquals(0, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));

		final StringWriter jsonContent = new StringWriter();
		jsonContent.append("{\n");
		jsonContent
				.append("\"63623334663430632d343930372d343661392d626636652d333765366635633735376338\": [[\"created\",\"1352884792199\",1352884792199], [\"id\",\"cb34f40c-4907-46a9-bf6e-37e6f5c757c8\",1352884792199], [\"modified\",\"1352884792199\",1352884792199], [\"name\",\"someday\",1352884856172000], [\"owner\",\"bborbe\",1352884792199]],\n");
		jsonContent
				.append("\"62636564633435302d663834382d336538392d623134342d353036396163646333646439\": [[\"created\",\"1352312862914\",1352312862916], [\"id\",\"bcedc450-f848-3e89-b144-5069acdc3dd9\",1352312862915], [\"modified\",\"1352312862914\",1352312862921], [\"name\",\"wow\",1352312862918], [\"owner\",\"bborbe\",1352312862919]]\n");
		jsonContent.append("}\n");

		importer.importJson(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY, jsonContent.toString());

		assertEquals(2, daoUtil.count(config.getKeySpace(), StorageTestUtil.COLUMNFAMILY));
	}
}
