package de.benjaminborbe.httpdownloader.api;

import java.net.HttpURLConnection;

public class HttpUtil {

	public HttpUtil() {
	}

	public boolean isAvailable(final HttpResponse httpResponse) {
		final Integer returnCode = httpResponse.getReturnCode();
		return returnCode != null && HttpURLConnection.HTTP_OK == returnCode;
	}

	public String getContentType(final HttpHeader httpHeader) {
		return httpHeader.getValue("Content-Type");
	}

	public boolean isHtml(final HttpHeader httpHeader) {
		final String contentType = getContentType(httpHeader);
		return contentType != null && contentType.startsWith("text/html");
	}

	public String getContent(final HttpResponse httpResponse) {
		return new String(httpResponse.getContent().getContent());
	}
}
