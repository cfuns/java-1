package de.benjaminborbe.proxy.core.dao;

import de.benjaminborbe.proxy.api.ProxyContent;
import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.api.ProxyConversationIdentifier;

import java.net.URL;

public class ProxyConversationImpl implements ProxyConversation {

	private final ProxyConversationIdentifier id;

	private final ProxyContent response;

	private final ProxyContent request;

	private URL url;

	public ProxyConversationImpl(
		final ProxyConversationIdentifier id,
		final ProxyContent request,
		final ProxyContent response
	) {
		this.id = id;
		this.request = request;
		this.response = response;
	}

	@Override
	public ProxyContent getRequest() {
		return request;
	}

	@Override
	public ProxyContent getResponse() {
		return response;
	}

	@Override
	public ProxyConversationIdentifier getId() {
		return id;
	}

	public void setUrl(final URL url) {
		this.url = url;
	}

	public URL getUrl() {
		return url;
	}
}
