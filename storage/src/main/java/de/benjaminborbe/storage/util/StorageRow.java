package de.benjaminborbe.storage.util;

import java.io.UnsupportedEncodingException;

public interface StorageRow {

	byte[] getKeyByte();

	String getKeyString() throws UnsupportedEncodingException;

	byte[] getByte(String columnName);

	String getString(String columnName) throws UnsupportedEncodingException;
}
