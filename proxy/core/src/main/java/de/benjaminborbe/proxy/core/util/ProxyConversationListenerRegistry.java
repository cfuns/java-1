package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.tools.registry.RegistryBase;

import javax.inject.Inject;

public class ProxyConversationListenerRegistry extends RegistryBase<ProxyConversationListener> {

	@Inject
	public ProxyConversationListenerRegistry(
		final ProxyConversationListenerHistory proxyConversationListenerHistory,
		final ProxyConversationListenerCrawler proxyConversationListenerCrawler
	) {
		super(proxyConversationListenerHistory, proxyConversationListenerCrawler);
	}
}
