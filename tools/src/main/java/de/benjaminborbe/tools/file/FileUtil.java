package de.benjaminborbe.tools.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.benjaminborbe.tools.util.StreamUtil;

public class FileUtil {

	private final StreamUtil streamUtil;

	public FileUtil(final StreamUtil streamUtil) {
		this.streamUtil = streamUtil;

	}

	public byte[] fileAsByteArray(final File file) throws IOException {
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		final FileInputStream inputStream = new FileInputStream(file);
		streamUtil.copy(inputStream, outputStream);
		return outputStream.toByteArray();
	}
}
