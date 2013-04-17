package de.benjaminborbe.storage.mock;

import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.ComparatorBase;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StorageColumnIteratorMock implements StorageColumnIterator {

	private final class StorageColumnComparator extends ComparatorBase<StorageColumn, String> implements Comparator<StorageColumn> {

		@Override
		public String getValue(final StorageColumn o) {
			try {
				return o.getColumnName() != null ? o.getColumnName().getString() : null;
			} catch (final UnsupportedEncodingException e) {
				return null;
			}
		}
	}

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

	private final Iterator<StorageColumn> i;

	public StorageColumnIteratorMock(final Map<StorageValue, StorageValue> data) {
		final List<StorageColumn> columns = new ArrayList<>();
		if (data != null) {
			for (final Entry<StorageValue, StorageValue> e : data.entrySet()) {
				columns.add(new StorageColumnImpl(e.getKey(), e.getValue()));
			}
		}
		Collections.sort(columns, new StorageColumnComparator());
		i = columns.iterator();
	}

	@Override
	public boolean hasNext() throws StorageException {
		return i.hasNext();
	}

	@Override
	public StorageColumn next() throws StorageException {
		return i.next();
	}

}
