package de.benjaminborbe.storage.tools;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;

import de.benjaminborbe.storage.api.StorageService;

public class TestDao extends DaoStorage<TestBean, TestIdentifier> {

	@Inject
	public TestDao(
			final Logger logger,
			final StorageService storageService,
			final Provider<TestBean> beanProvider,
			final TestBeanMapper mapper,
			final TestIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	@Override
	protected String getColumnFamily() {
		return "test";
	}

}
