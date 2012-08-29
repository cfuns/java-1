package de.benjaminborbe.tools.util;

import java.io.IOException;
import java.io.OutputStream;

public interface ResourceUtil {

	String getResourceContentString(String path) throws IOException;

	byte[] getResourceContentByteArray(String path) throws IOException;

	void copyResourceToOutputStream(String path, OutputStream outputStream) throws IOException;

}
