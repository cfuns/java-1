package de.benjaminborbe.wiki.dao;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.tools.DaoStorage;
import de.benjaminborbe.wiki.api.WikiSpaceIdentifier;

@Singleton
public class WikiSpaceDaoImpl extends DaoStorage<WikiSpaceBean, WikiSpaceIdentifier> implements WikiSpaceDao {

	private static final String COLUMN_FAMILY = "wiki_space";

	@Inject
	public WikiSpaceDaoImpl(
			final Logger logger,
			final StorageService storageService,
			final Provider<WikiSpaceBean> beanProvider,
			final WikiSpaceBeanMapper mapper,
			final WikiSpaceIdentifierBuilder identifierBuilder) {
		super(logger, storageService, beanProvider, mapper, identifierBuilder);
	}

	@Override
	protected String getColumnFamily() {
		return COLUMN_FAMILY;
	}

	@Override
	public boolean existsSpaceWithName(final String spaceName) throws StorageException {
		return exists(spaceName);
	}

}
