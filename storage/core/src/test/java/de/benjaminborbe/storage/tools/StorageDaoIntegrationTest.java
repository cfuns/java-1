package de.benjaminborbe.storage.tools;

import com.google.inject.Injector;
import de.benjaminborbe.storage.config.StorageConfig;
import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.storage.util.StorageTestUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StorageDaoIntegrationTest {

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
		} catch (final IOException e) {
			notFound = true;
		} finally {
			try {
				socket.close();
			} catch (final IOException e) {
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
	public void testPerformance() throws Exception {
		if (notFound)
			return;
		final Injector injector = GuiceInjectorBuilder.getInjector(new StorageModulesMock());
		final TestDao testDao = injector.getInstance(TestDao.class);

		final int limit = 1;
		for (int i = 0; i < limit; ++i) {

			final TestBean testBean = testDao.create();
			testBean.setId(new TestIdentifier(String.valueOf(i)));
			testBean.setName("name_" + i);
			testDao.save(testBean);
		}

		final EntityIterator<TestBean> i = testDao.getEntityIterator();
		int counter = 0;
		while (i.hasNext()) {
			final TestBean bean = i.next();
			assertNotNull(bean);
			counter++;
		}
		assertEquals(limit, counter);
	}
}
