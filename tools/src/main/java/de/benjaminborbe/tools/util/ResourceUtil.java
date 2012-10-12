package de.benjaminborbe.tools.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface ResourceUtil {

	String getResourceContentAsString(String path) throws IOException;

	byte[] getResourceContentAsByteArray(String path) throws IOException;

	InputStream getResourceContentAsInputStream(String path) throws IOException;

	void copyResourceToOutputStream(String path, OutputStream outputStream) throws IOException;

}
