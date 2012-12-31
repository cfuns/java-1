package de.benjaminborbe.storage.mock;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.benjaminborbe.storage.api.StorageColumn;
import de.benjaminborbe.storage.api.StorageColumnIterator;
import de.benjaminborbe.storage.api.StorageException;

public class StorageColumnIteratorMock implements StorageColumnIterator {

	private final class StorageColumnImpl implements StorageColumn {

		private final String key;

		private final String value;

		public StorageColumnImpl(final String key, final String value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public byte[] getColumnNameByte() {
			return key.getBytes();
		}

		@Override
		public String getColumnNameString() throws UnsupportedEncodingException {
			return key;
		}

		@Override
		public byte[] getColumnValueByte() {
			return value.getBytes();
		}

		@Override
		public String getColumnValueString() throws UnsupportedEncodingException {
			return value;
		}
	}

	private final Iterator<Entry<String, String>> i;

	public StorageColumnIteratorMock(final Map<String, String> data) {
		i = data.entrySet().iterator();
	}

	@Override
	public boolean hasNext() throws StorageException {
		return i.hasNext();
	}

	@Override
	public StorageColumn next() throws StorageException {
		final Entry<String, String> next = i.next();
		return new StorageColumnImpl(next.getKey(), next.getValue());
	}

}
