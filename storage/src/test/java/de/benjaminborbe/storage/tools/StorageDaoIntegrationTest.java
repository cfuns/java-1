package de.benjaminborbe.storage.tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Injector;

import de.benjaminborbe.storage.guice.StorageModulesMock;
import de.benjaminborbe.storage.util.StorageTestUtil;
import de.benjaminborbe.tools.guice.GuiceInjectorBuilder;

public class StorageDaoIntegrationTest {

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
		final TestDao testDao = injector.getInstance(TestDao.class);

		final int limit = 10000;
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
