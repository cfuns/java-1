package de.benjaminborbe.storage.tools;

import de.benjaminborbe.api.Identifier;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageIterator;
import de.benjaminborbe.storage.api.StorageRow;
import de.benjaminborbe.storage.api.StorageRowIterator;
import de.benjaminborbe.storage.api.StorageService;
import de.benjaminborbe.storage.api.StorageValue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public abstract class ManyToManyRelationStorage<A extends Identifier<?>, B extends Identifier<?>> implements ManyToManyRelation<A, B> {

	private final class StorageIdIterator implements StorageIterator {

		private final StorageRowIterator i;

		private final StorageValue key;

		private StorageIdIterator(final StorageRowIterator i, final StorageValue key) {
			this.i = i;
			this.key = key;
		}

		@Override
		public boolean hasNext() throws StorageException {
			return i.hasNext();
		}

		@Override
		public StorageValue next() throws StorageException {
			final StorageRow row = i.next();
			return row.getValue(key);
		}
	}

	private final String KEY = "exists";

	private final String KEY_A = "key_a";

	private final String KEY_B = "key_b";

	private final String VALUE = "true";

	private final Logger logger;

	private final StorageService storageService;

	@Inject
	public ManyToManyRelationStorage(final Logger logger, final StorageService storageService) {
		this.logger = logger;
		this.storageService = storageService;
	}

	private String getEncoding() {
		return storageService.getEncoding();
	}

	protected abstract String getColumnFamily();

	protected StorageValue buildKey(final Identifier<?>... identifiers) {
		return new StorageValue(StringUtils.join(identifiers, "-"), getEncoding());
	}

	@Override
	public void add(final A identifierA, final B identifierB) throws StorageException {
		logger.trace("add " + identifierA + " " + identifierB);
		final StorageValue id = buildKey(identifierA, identifierB);
		final StorageValueMap data = new StorageValueMap(getEncoding());
		data.add(KEY, VALUE);
		data.add(KEY_A, String.valueOf(identifierA));
		data.add(KEY_B, String.valueOf(identifierB));
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
		return new StorageValue(VALUE, getEncoding()).equals(storageService.get(getColumnFamily(), id, new StorageValue(KEY, getEncoding())));
	}

	@Override
	public StorageIterator getA(final A identifierA) throws StorageException {
		final StorageValue c = new StorageValue(KEY_B, getEncoding());
		final List<StorageValue> columnNames = Arrays.asList(c);
		final StorageRowIterator i = storageService.rowIterator(getColumnFamily(), columnNames, new StorageValueMap(getEncoding()).add(KEY_A, String.valueOf(identifierA)));
		return new StorageIdIterator(i, c);
	}

	@Override
	public StorageIterator getB(final B identifierB) throws StorageException {
		final StorageValue c = new StorageValue(KEY_A, getEncoding());
		final List<StorageValue> columnNames = Arrays.asList(c);
		final StorageRowIterator i = storageService.rowIterator(getColumnFamily(), columnNames, new StorageValueMap(getEncoding()).add(KEY_B, String.valueOf(identifierB)));
		return new StorageIdIterator(i, c);
	}

	@Override
	public void removeA(final A identifierA) throws StorageException {
		final StorageIterator i = storageService.keyIterator(getColumnFamily(), new StorageValueMap(getEncoding()).add(KEY_A, String.valueOf(identifierA)));
		while (i.hasNext()) {
			delete(i.next());
		}
	}

	@Override
	public void removeB(final B identifierB) throws StorageException {
		final StorageIterator i = storageService.keyIterator(getColumnFamily(), new StorageValueMap(getEncoding()).add(KEY_B, String.valueOf(identifierB)));
		while (i.hasNext()) {
			delete(i.next());
		}
	}

	private void delete(final StorageValue id) throws StorageException {
		storageService.delete(getColumnFamily(), id);
	}

}
