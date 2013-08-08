package de.benjaminborbe.httpdownloader.tools;

import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.httpdownloader.api.HttpMethod;
import de.benjaminborbe.httpdownloader.api.HttpRequest;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.net.URL;
import java.util.Map;

public class HttpRequestDto implements HttpRequest {

	private String username;

	private String password;

	private HttpMethod httpMethod;

	private Integer timeout;

	private URL url;

	private HttpHeader header;

	private Map<String, String> parameter;

	private Boolean secure;

	private Boolean followRedirects;

	public void setFollowRedirects(final Boolean followRedirects) {
		this.followRedirects = followRedirects;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(final Integer timeout) {
		this.timeout = timeout;
	}

	@Override
	public Map<String, String> getParameter() {
		return parameter;
	}

	public void setParameter(final Map<String, String> parameter) {
		this.parameter = parameter;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	public HttpHeader getHeader() {
		return header;
	}

	public void setHeader(final HttpHeader header) {
		this.header = header;
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(final HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		final HttpRequestDto that = (HttpRequestDto) o;

		if (timeout != that.timeout) return false;
		if (header != null ? !header.equals(that.header) : that.header != null) return false;
		if (httpMethod != that.httpMethod) return false;
		if (url != null ? !url.equals(that.url) : that.url != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = httpMethod != null ? httpMethod.hashCode() : 0;
		result = 31 * result + timeout;
		result = 31 * result + (url != null ? url.hashCode() : 0);
		result = 31 * result + (header != null ? header.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("url", url)
			.append("timeout", timeout)
			.append("header", header)
			.toString();
	}

	public Boolean getSecure() {
		return secure;
	}

	@Override
	public Boolean getFollowRedirects() {
		return followRedirects;
	}

	public void setSecure(final Boolean secure) {
		this.secure = secure;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
}
