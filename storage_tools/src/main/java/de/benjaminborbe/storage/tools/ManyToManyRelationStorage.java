package de.benjaminborbe.storage.tools;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageService;

public abstract class ManyToManyRelationStorage<A extends Identifier<?>, B extends Identifier<?>> implements ManyToManyRelation<A, B> {

	private static final String KEY = "exists";

	private static final String VALUE = "true";

	private final Logger logger;

	private final StorageService storageService;

	@Inject
	public ManyToManyRelationStorage(final Logger logger, final StorageService storageService) {
		this.logger = logger;
		this.storageService = storageService;
	}

	protected abstract String getColumnFamily();

	protected String buildKey(final Identifier<?>... identifiers) {
		return StringUtils.join(identifiers, "-");
	}

	@Override
	public void add(final A identifierA, final B identifierB) throws StorageException {
		logger.debug("add " + identifierA + " " + identifierB);
		final String id = buildKey(identifierA, identifierB);
		storageService.set(getColumnFamily(), id, KEY, VALUE);
	}

	@Override
	public void remove(final A identifierA, final B identifierB) throws StorageException {
		logger.debug("remove " + identifierA + " " + identifierB);
		final String id = buildKey(identifierA, identifierB);
		storageService.delete(getColumnFamily(), id, KEY);
	}

	@Override
	public boolean exists(final A identifierA, final B identifierB) throws StorageException {
		logger.debug("exists " + identifierA + " " + identifierB);
		final String id = buildKey(identifierA, identifierB);
		return VALUE.equals(storageService.get(getColumnFamily(), id, KEY));
	}
}
