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
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.map.MapChain;

public abstract class ManyToManyRelationStorage<A extends Identifier<?>, B extends Identifier<?>> implements ManyToManyRelation<A, B> {

	private final class StorageIdIterator implements StorageIterator {

		private final StorageIterator i;

		private final StorageValue key;

		private StorageIdIterator(final StorageIterator i, final StorageValue key) {
			this.i = i;
			this.key = key;
		}

		@Override
		public boolean hasNext() throws StorageException {
			return i.hasNext();
		}

		@Override
		public StorageValue next() throws StorageException {
			final StorageValue id = i.next();
			return storageService.get(getColumnFamily(), id, key);
		}
	}

	private final StorageValue KEY;

	private final StorageValue KEY_A;

	private final StorageValue KEY_B;

	private final StorageValue VALUE;

	private final Logger logger;

	private final StorageService storageService;

	private final String encoding;

	@Inject
	public ManyToManyRelationStorage(final Logger logger, final StorageService storageService) throws StorageException {
		this.logger = logger;
		this.storageService = storageService;
		this.encoding = storageService.getEncoding();
		KEY = new StorageValue("exists", encoding);
		KEY_A = new StorageValue("key_a", encoding);
		KEY_B = new StorageValue("key_b", encoding);
		VALUE = new StorageValue("true", encoding);
	}

	protected abstract String getColumnFamily();

	protected StorageValue buildKey(final Identifier<?>... identifiers) {
		return new StorageValue(StringUtils.join(identifiers, "-"), encoding);
	}

	@Override
	public void add(final A identifierA, final B identifierB) throws StorageException {
		logger.trace("add " + identifierA + " " + identifierB);
		final StorageValue id = buildKey(identifierA, identifierB);
		final Map<StorageValue, StorageValue> data = new HashMap<StorageValue, StorageValue>();
		data.put(KEY, VALUE);
		data.put(KEY_A, new StorageValue(String.valueOf(identifierA), encoding));
		data.put(KEY_B, new StorageValue(String.valueOf(identifierB), encoding));
		storageService.set(getColumnFamily(), id, data);
	}

	@Override
	public void remove(final A identifierA, final B identifierB) throws StorageException {
		logger.trace("remove " + identifierA + " " + identifierB);
		final StorageValue id = buildKey(identifierA, identifierB);
		delete(id);
	}

	@Override
	public boolean exists(final A identifierA, final B identifierB) throws StorageException {
		logger.trace("exists " + identifierA + " " + identifierB);
		final StorageValue id = buildKey(identifierA, identifierB);
		return VALUE.equals(storageService.get(getColumnFamily(), id, KEY));
	}

	@Override
	public StorageIterator getA(final A identifierA) throws StorageException {
		final StorageIterator i = storageService.keyIterator(getColumnFamily(),
				new MapChain<StorageValue, StorageValue>().add(KEY_A, new StorageValue(String.valueOf(identifierA), encoding)));
		return new StorageIdIterator(i, KEY_B);
	}

	@Override
	public StorageIterator getB(final B identifierB) throws StorageException {
		final StorageIterator i = storageService.keyIterator(getColumnFamily(),
				new MapChain<StorageValue, StorageValue>().add(KEY_B, new StorageValue(String.valueOf(identifierB), encoding)));
		return new StorageIdIterator(i, KEY_A);
	}

	@Override
	public void removeA(final A identifierA) throws StorageException {
		final StorageIterator i = storageService.keyIterator(getColumnFamily(),
				new MapChain<StorageValue, StorageValue>().add(KEY_A, new StorageValue(String.valueOf(identifierA), encoding)));
		while (i.hasNext()) {
			delete(i.next());
		}
	}

	@Override
	public void removeB(final B identifierB) throws StorageException {
		final StorageIterator i = storageService.keyIterator(getColumnFamily(),
				new MapChain<StorageValue, StorageValue>().add(KEY_B, new StorageValue(String.valueOf(identifierB), encoding)));
		while (i.hasNext()) {
			delete(i.next());
		}
	}

	private void delete(final StorageValue id) throws StorageException {
		storageService.delete(getColumnFamily(), id);
	}

}
