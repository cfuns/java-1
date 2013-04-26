package de.benjaminborbe.httpdownloader.api;

public class HttpUtil {

	public HttpUtil() {
	}

	public boolean isAvailable(HttpResponse httpResponse) {
		return 200 == httpResponse.getReturnCode();
	}

	public String getContentType(HttpHeader httpHeader) {
		return httpHeader.getValue("Content-Type");
	}

	public boolean isHtml(HttpHeader httpHeader) {
		final String contentType = getContentType(httpHeader);
		return contentType != null && contentType.startsWith("text/html");
	}

	public String getContent(HttpResponse httpResponse) {
		return new String(httpResponse.getContent().getContent());
	}
}
