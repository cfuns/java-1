package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Map;

import de.benjaminborbe.storage.api.StorageRow;

public class StorageRowImpl implements StorageRow {

	private final byte[] key;

	private final Map<String, byte[]> data;

	private final String encoding;

	public StorageRowImpl(final String encoding, final byte[] key, final Map<String, byte[]> data) {
		this.encoding = encoding;
		this.key = key;
		this.data = data;
	}

	@Override
	public byte[] getKeyByte() {
		return key;
	}

	@Override
	public String getKeyString() throws UnsupportedEncodingException {
		return new String(getKeyByte(), encoding);
	}

	@Override
	public byte[] getByte(final String columnName) {
		return data.get(columnName);
	}

	@Override
	public String getString(final String columnName) throws UnsupportedEncodingException {
		return new String(getByte(columnName), encoding);
	}

	@Override
	public Collection<String> getColumnNames() {
		return data.keySet();
	}

}
