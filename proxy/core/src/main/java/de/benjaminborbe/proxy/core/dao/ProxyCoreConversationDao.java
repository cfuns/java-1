package de.benjaminborbe.proxy.core.dao;

import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.api.ProxyConversationIdentifier;

import java.util.Collection;

public interface ProxyCoreConversationDao {

	Collection<ProxyConversationIdentifier> getProxyConversationIdentifiers();

	Collection<ProxyConversation> getProxyConversations();

	ProxyConversation getProxyConversation(ProxyConversationIdentifier id);

	void add(ProxyConversation proxyConversation);

}
