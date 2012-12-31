package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;

import de.benjaminborbe.storage.api.StorageColumn;

public class StorageColumnImpl implements StorageColumn {

	private final String encoding;

	private final byte[] name;

	private final byte[] value;

	public StorageColumnImpl(final String encoding, final byte[] name, final byte[] value) {
		this.encoding = encoding;
		this.name = name;
		this.value = value;
	}

	@Override
	public byte[] getColumnNameByte() {
		return name;
	}

	@Override
	public String getColumnNameString() throws UnsupportedEncodingException {
		return new String(name, encoding);
	}

	@Override
	public byte[] getColumnValueByte() {
		return value;
	}

	@Override
	public String getColumnValueString() throws UnsupportedEncodingException {
		return new String(value, encoding);
	}

}
