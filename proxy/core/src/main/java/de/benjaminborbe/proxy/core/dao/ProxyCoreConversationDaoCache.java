package de.benjaminborbe.proxy.core.dao;

import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.api.ProxyConversationIdentifier;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ProxyCoreConversationDaoCache implements ProxyCoreConversationDao {

	private final Map<ProxyConversationIdentifier, ProxyConversation> data = new HashMap<ProxyConversationIdentifier, ProxyConversation>();

	@Inject
	public ProxyCoreConversationDaoCache() {
	}

	@Override
	public Collection<ProxyConversationIdentifier> getProxyConversationIdentifiers() {
		return data.keySet();
	}

	@Override
	public Collection<ProxyConversation> getProxyConversations() {
		return data.values();
	}

	@Override
	public ProxyConversation getProxyConversation(final ProxyConversationIdentifier id) {
		return data.get(id);
	}

	@Override
	public void add(final ProxyConversation proxyConversation) {
		data.put(proxyConversation.getId(), proxyConversation);
	}

}
