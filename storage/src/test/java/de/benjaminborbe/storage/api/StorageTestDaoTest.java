package de.benjaminborbe.storage.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.storage.mock.StorageServiceMock;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class StorageTestDaoTest {

	@Test
	public void testFields() {
		final StorageTestDao dao = getDao();
		final TestBean entity = dao.create();
		final List<String> fieldNames = dao.getFieldNames(entity);
		assertNotNull(fieldNames);
		Collections.sort(fieldNames);
		assertEquals(2, fieldNames.size());
		assertEquals("id", fieldNames.get(0));
		assertEquals("name", fieldNames.get(1));
	}

	protected StorageTestDao getDao() {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StorageService storageService = new StorageServiceMock();
		final ParseUtil parseUtil = new ParseUtilImpl();
		final Provider<TestBean> beanProvider = new Provider<TestBean>() {

			@Override
			public TestBean get() {
				return new TestBean();
			}
		};

		final StorageTestDao dao = new StorageTestDao(logger, storageService, parseUtil, beanProvider);
		return dao;
	}

	@Test
	public void testCrud() {
		final StorageTestDao dao = getDao();

		final Long id = 1l;
		final String name = "name";

		// create
		{
			final TestBean bean = dao.create();
			bean.setId(id);
			bean.setName(name);
			dao.save(bean);
		}

		// load
		{
			final TestBean bean = dao.load(id);
			assertEquals(id, bean.getId());
			assertEquals(name, bean.getName());
		}

		// delete
		{
			final TestBean bean = dao.load(id);
			dao.delete(bean);
		}

		// load
		{
			assertNull(dao.load(id));
		}
	}

	private final class StorageTestDao extends StorageDao<TestBean> {

		@Inject
		public StorageTestDao(final Logger logger, final StorageService storageService, final ParseUtil parseUtil, final Provider<TestBean> beanProvider) {
			super(logger, storageService, parseUtil, beanProvider);
		}

	}

}
