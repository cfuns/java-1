package de.benjaminborbe.proxy.api;

import java.util.Collection;

public interface ProxyService {

	void start() throws ProxyServiceException;

	void stop() throws ProxyServiceException;

	Collection<ProxyConversationIdentifier> getConversations() throws ProxyServiceException;

	ProxyConversation getProxyConversation(ProxyConversationIdentifier proxyConversationIdentifier) throws ProxyServiceException;

	ProxyConversationIdentifier createProxyConversationIdentifier(String id) throws ProxyServiceException;

	Integer getServerPort();
}
