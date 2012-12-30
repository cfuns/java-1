package de.benjaminborbe.storage.tools;

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

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.mock.StorageServiceMock;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.guice.ProviderMock;
import de.benjaminborbe.tools.mapper.mapobject.MapObjectMapper;

public class StorageDaoUnitTest {

	@Test
	public void testFields() throws Exception {
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

		final StorageService storageService = new StorageServiceMock(logger);
		final Provider<TestBean> beanProvider = new ProviderMock<TestBean>(TestBean.class);
		final MapObjectMapper<TestBean> mapper = new TestBeanMapper(beanProvider);
		final IdentifierBuilder<String, TestIdentifier> builder = new TestIdentifierBuilder();
		final CalendarUtil calendarUtil = EasyMock.createMock(CalendarUtil.class);
		EasyMock.replay(calendarUtil);

		return new StorageTestDao(logger, storageService, beanProvider, mapper, builder, calendarUtil);
	}

	@Test
	public void testCrud() throws Exception {
		final StorageTestDao dao = getDao();

		final String id = "1";
		final String name = "name";

		// create
		{
			final TestBean bean = dao.create();
			bean.setId(new TestIdentifier(id));
			bean.setName(name);
			dao.save(bean);
		}

		// load
		{
			final TestBean bean = dao.load(new TestIdentifier(id));
			assertEquals(id, bean.getId().getId());
			assertEquals(name, bean.getName());
		}

		// delete
		{
			final TestBean bean = dao.load(new TestIdentifier(id));
			dao.delete(bean);
		}

		// load
		{
			assertNull(dao.load(new TestIdentifier(id)));
		}
	}

	private final class TestIdentifierBuilder implements IdentifierBuilder<String, TestIdentifier> {

		@Override
		public TestIdentifier buildIdentifier(final String value) {
			return new TestIdentifier(value);
		}
	}

	private final class StorageTestDao extends DaoStorage<TestBean, TestIdentifier> {

		private static final String COLUMNFAMILY = "test";

		@Inject
		public StorageTestDao(
				final Logger logger,
				final StorageService storageService,
				final Provider<TestBean> beanProvider,
				final MapObjectMapper<TestBean> mapper,
				final IdentifierBuilder<String, TestIdentifier> identifierBuilder,
				final CalendarUtil calendarUtil) {
			super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
		}

		@Override
		protected String getColumnFamily() {
			return COLUMNFAMILY;
		}

	}

}
