package de.benjaminborbe.httpdownloader.tools;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpResponse;
import de.benjaminborbe.tools.stream.StreamUtil;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;

public class HttpUtil {

	public static final String DEFAULT_CHARSET = "UTF8";

	public static final String CONTENT_ENCODING = "Content-Encoding";

	public static final String CONTENT_TYPE = "Content-Type";

	private final Logger logger;

	private final StreamUtil streamUtil;

	@Inject
	public HttpUtil(final Logger logger, final StreamUtil streamUtil) {
		this.logger = logger;
		this.streamUtil = streamUtil;
	}

	public boolean isAvailable(final HttpResponse httpResponse) {
		final Integer returnCode = httpResponse.getReturnCode();
		return returnCode != null && HttpURLConnection.HTTP_OK == returnCode;
	}

	public String getContentType(final HttpHeader httpHeader) {
		return httpHeader != null ? httpHeader.getValue(CONTENT_TYPE) : null;
	}

	public boolean isHtml(final HttpHeader httpHeader) {
		final String contentType = getContentType(httpHeader);
		return contentType != null && contentType.startsWith("text/html");
	}

	public String getContent(final HttpResponse httpResponse) throws IOException {
		final HttpContent httpContent = httpResponse.getContent();
		if (httpContent == null) {
			logger.trace("httpContent is null");
			return null;
		}
		final byte[] content = httpContent.getContent();
		if (content == null) {
			logger.trace("content is null");
			return null;
		}
		final HttpHeader header = httpResponse.getHeader();
		final String charset = getCharset(header);
		if ("gzip".equals(getContentEncoding(header))) {
			try {
				final GZIPInputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(content));
				final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				streamUtil.copy(inputStream, outputStream);
				return new String(outputStream.toByteArray(), charset);
			} catch (java.util.zip.ZipException e) {
				logger.warn("getContent as gzip failed", e);
				return new String(content, charset);
			}
		} else {
			return new String(content, charset);
		}
	}

	private String getContentEncoding(final HttpHeader header) {
		return header != null ? header.getValue(CONTENT_ENCODING) : null;
	}

	private String getCharset(final HttpHeader header) {
		final String contentType = getContentType(header);
		if (contentType != null) {
			final String s = "charset=";
			final int pos = contentType.indexOf(s);
			if (pos != -1) {
				return contentType.substring(pos + s.length());
			}
		}
		return DEFAULT_CHARSET;
	}

	public String getCharset(final HttpResponse httpResponse) {
		return getCharset(httpResponse.getHeader());
	}
}
