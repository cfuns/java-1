package de.benjaminborbe.tools.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ResourceUtilImpl implements ResourceUtil {

	private final StreamUtil streamUtil;

	@Inject
	public ResourceUtilImpl(final StreamUtil streamUtil) {
		this.streamUtil = streamUtil;
	}

	@Override
	public String getResourceContentString(final String path) throws IOException {
		InputStream is = null;
		BufferedReader br = null;
		try {
			final StringBuffer sb = new StringBuffer();
			is = getClass().getClassLoader().getResourceAsStream(path);
			if (is == null) {
				throw new IOException("Util.getResourceContentString() - file " + path + " not found.");
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
		}
		finally {
			if (is != null) {
				is.close();
			}
			if (br != null) {
				br.close();
			}
		}
	}

	@Override
	public byte[] getResourceContentByteArray(final String path) throws IOException {
		final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		streamUtil.copy(inputStream, outputStream);
		return outputStream.toByteArray();
	}
}
