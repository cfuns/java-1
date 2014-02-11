package de.benjaminborbe.httpdownloader.tools;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpMethod;
import de.benjaminborbe.httpdownloader.api.HttpRequest;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HttpRequestBuilder {

	private final HttpRequestDto httpRequest;

	private final HttpHeaderDto httpHeader;

	public HttpRequestBuilder(final URL url) {
		httpRequest = new HttpRequestDto();
		httpRequest.setUrl(url);
		httpHeader = new HttpHeaderDto();
		httpRequest.setHeader(httpHeader);
	}

	public HttpRequestBuilder copyRequest(final HttpRequest request) {
		copyHeader(request.getHeader());
		httpRequest.setFollowRedirects(request.getFollowRedirects());
		httpRequest.setHttpMethod(request.getHttpMethod());
		httpRequest.setParameter(request.getParameter());
		httpRequest.setPassword(request.getPassword());
		httpRequest.setSecure(request.getSecure());
		httpRequest.setTimeout(request.getTimeout());
		httpRequest.setUsername(request.getUsername());
		return this;
	}

	public HttpRequestBuilder copyHeader(final HttpHeader header) {
		for (final String key : header.getKeys()) {
			httpHeader.setHeader(key, header.getValues(key));
		}
		return this;
	}

	public HttpRequest build() {
		return httpRequest;
	}

	public HttpRequestBuilder addSecure(final boolean secure) {
		httpRequest.setSecure(secure);
		return this;
	}

	public HttpRequestBuilder addFollowRedirects(final boolean followRedirects) {
		httpRequest.setFollowRedirects(followRedirects);
		return this;
	}

	public HttpRequestBuilder addTimeout(final int timeout) {
		httpRequest.setTimeout(timeout);
		return this;
	}

	public HttpRequestBuilder addParameters(final Map<String, String> parameters) {
		httpRequest.setParameter(parameters);
		return this;
	}

	public HttpRequestBuilder addCookies(final Map<String, String> cookies) {
		return addHeader("Cookie", buildCookieString(cookies));
	}

	public HttpRequestBuilder addHeader(final String key, final String value) {
		httpHeader.setHeader(key, value);
		return this;
	}

	private String buildCookieString(final Map<String, String> cookies) {
		boolean first = true;
		final StringWriter result = new StringWriter();
		final List<String> keys = new ArrayList<String>(cookies.keySet());
		Collections.sort(keys);
		for (final String key : keys) {
			if (first) {
				first = false;
			} else {
				result.append("; ");
			}
			result.append(key);
			result.append("=");
			result.append(cookies.get(key));
		}
		return result.toString();
	}

	public HttpRequestBuilder addHttpMethod(final HttpMethod httpMethod) {
		httpRequest.setHttpMethod(httpMethod);
		return this;
	}
}
