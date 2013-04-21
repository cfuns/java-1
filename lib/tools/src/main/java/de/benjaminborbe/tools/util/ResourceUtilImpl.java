package de.benjaminborbe.tools.util;

import de.benjaminborbe.tools.stream.StreamUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

@Singleton
public class ResourceUtilImpl implements ResourceUtil {

	private final StreamUtil streamUtil;

	@Inject
	public ResourceUtilImpl(final StreamUtil streamUtil) {
		this.streamUtil = streamUtil;
	}

	@Override
	public void copyResourceToOutputStream(final String path, final OutputStream outputStream) throws IOException {
		InputStream is = null;
		try {
			is = getClass().getClassLoader().getResourceAsStream(path);
			if (is == null) {
				throw new IOException("file " + path + " not found.");
			}
			streamUtil.copy(is, outputStream);
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	@Override
	public String getResourceContentAsString(final String path) throws IOException {
		InputStream is = null;
		BufferedReader br = null;
		try {
			final StringBuilder sb = new StringBuilder();
			is = getClass().getClassLoader().getResourceAsStream(path);
			if (is == null) {
				throw new IOException("file " + path + " not found.");
			}
			br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			br.close();
			br = null;
			return sb.toString();
		} finally {
			if (is != null) {
				is.close();
			}
			if (br != null) {
				br.close();
			}
		}
	}

	@Override
	public InputStream getResourceContentAsInputStream(final String path) throws IOException {
		return getClass().getClassLoader().getResourceAsStream(path);
	}

	@Override
	public byte[] getResourceContentAsByteArray(final String path) throws IOException {
		final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		streamUtil.copy(inputStream, outputStream);
		return outputStream.toByteArray();
	}
}
