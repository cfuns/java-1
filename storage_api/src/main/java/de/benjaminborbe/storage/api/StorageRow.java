package de.benjaminborbe.storage.api;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

public interface StorageRow {

	byte[] getKeyByte();

	String getKeyString() throws UnsupportedEncodingException;

	byte[] getByte(String columnName);

	String getString(String columnName) throws UnsupportedEncodingException;

	Collection<String> getColumnNames();
}
