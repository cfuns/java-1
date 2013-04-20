package de.benjaminborbe.storage.tools;

import org.slf4j.Logger;

import javax.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.date.CalendarUtil;

public class TestDao extends DaoStorage<TestBean, TestIdentifier> {

	@Inject
	public TestDao(
			final Logger logger,
			final StorageService storageService,
			final Provider<TestBean> beanProvider,
			final TestBeanMapper mapper,
			final TestIdentifierBuilder identifierBuilder,
			final CalendarUtil calendarUtil) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder, calendarUtil);
	}

	@Override
	protected String getColumnFamily() {
		return "test";
	}

}
