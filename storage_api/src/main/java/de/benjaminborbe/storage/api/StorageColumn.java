package de.benjaminborbe.storage.api;

import java.io.UnsupportedEncodingException;

public interface StorageColumn {

	byte[] getColumnNameByte();

	String getColumnNameString() throws UnsupportedEncodingException;

	byte[] getColumnValueByte();

	String getColumnValueString() throws UnsupportedEncodingException;

}
