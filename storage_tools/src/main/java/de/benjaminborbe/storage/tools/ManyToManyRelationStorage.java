package de.benjaminborbe.storage.tools;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.tools.map.MapChain;

public abstract class ManyToManyRelationStorage<A extends Identifier<?>, B extends Identifier<?>> implements ManyToManyRelation<A, B> {

	private static final String KEY = "exists";

	private static final String KEY_A = "key_a";

	private static final String KEY_B = "key_b";

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
		logger.trace("add " + identifierA + " " + identifierB);
		final String id = buildKey(identifierA, identifierB);
		final Map<String, String> data = new HashMap<String, String>();
		data.put(KEY, VALUE);
		data.put(KEY_A, String.valueOf(identifierA));
		data.put(KEY_B, String.valueOf(identifierB));
		storageService.set(getColumnFamily(), id, data);
	}

	@Override
	public void remove(final A identifierA, final B identifierB) throws StorageException {
		logger.trace("remove " + identifierA + " " + identifierB);
		final String id = buildKey(identifierA, identifierB);
		delete(id);
	}

	@Override
	public boolean exists(final A identifierA, final B identifierB) throws StorageException {
		logger.trace("exists " + identifierA + " " + identifierB);
		final String id = buildKey(identifierA, identifierB);
		return VALUE.equals(storageService.get(getColumnFamily(), id, KEY));
	}

	@Override
	public void removeA(final A identifierA) throws StorageException {
		final StorageIterator i = storageService.list(getColumnFamily(), new MapChain<String, String>().add(KEY_A, String.valueOf(identifierA)));
		while (i.hasNext()) {
			delete(i.nextString());
		}
	}

	@Override
	public void removeB(final B identifierB) throws StorageException {
		final StorageIterator i = storageService.list(getColumnFamily(), new MapChain<String, String>().add(KEY_B, String.valueOf(identifierB)));
		while (i.hasNext()) {
			delete(i.nextString());
		}
	}

	private void delete(final String id) throws StorageException {
		storageService.delete(getColumnFamily(), id, KEY, KEY_A, KEY_B);
	}

}
