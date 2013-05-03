package de.benjaminborbe.proxy.mock;

import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.api.ProxyConversationIdentifier;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;

import java.util.Collection;

public class ProxyServiceMock implements ProxyService {

	public ProxyServiceMock() {
	}

	@Override
	public void start() throws ProxyServiceException {
	}

	@Override
	public void stop() throws ProxyServiceException {
	}

	@Override
	public Collection<ProxyConversationIdentifier> getConversations() throws ProxyServiceException {
		return null;
	}

	@Override
	public ProxyConversation getProxyConversation(final ProxyConversationIdentifier proxyConversationIdentifier) throws ProxyServiceException {
		return null;
	}

	@Override
	public ProxyConversationIdentifier createProxyConversationIdentifier(final String id) throws ProxyServiceException {
		return null;
	}

	@Override
	public Integer getServerPort() {
		return null;
	}
}
