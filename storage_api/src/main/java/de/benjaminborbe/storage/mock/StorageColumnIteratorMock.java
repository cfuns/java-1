package de.benjaminborbe.storage.mock;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.EmptyIterator;

public class StorageColumnIteratorMock implements StorageColumnIterator {

	private final class StorageColumnImpl implements StorageColumn {

		private final StorageValue key;

		private final StorageValue value;

		public StorageColumnImpl(final StorageValue key, final StorageValue value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public StorageValue getColumnName() {
			return key;
		}

		@Override
		public StorageValue getColumnValue() {
			return value;
		}

	}

	private final Iterator<Entry<StorageValue, StorageValue>> i;

	public StorageColumnIteratorMock(final Map<StorageValue, StorageValue> data) {
		if (data != null) {
			i = data.entrySet().iterator();
		}
		else {
			i = new EmptyIterator<Map.Entry<StorageValue, StorageValue>>();
		}
	}

	@Override
	public boolean hasNext() throws StorageException {
		return i.hasNext();
	}

	@Override
	public StorageColumn next() throws StorageException {
		final Entry<StorageValue, StorageValue> next = i.next();
		return new StorageColumnImpl(next.getKey(), next.getValue());
	}

}
