package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.proxy.api.ProxyConversation;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ProxyConversationNotifier {

	private final Logger logger;

	private final ProxyConversationListenerRegistry proxyConversationListenerRegistry;

	@Inject
	public ProxyConversationNotifier(final Logger logger, ProxyConversationListenerRegistry proxyConversationListenerRegistry) {
		this.logger = logger;
		this.proxyConversationListenerRegistry = proxyConversationListenerRegistry;
	}

	public void onProxyConversationCompleted(ProxyConversation proxyConversation) {
		for (ProxyConversationListener proxyConversationListener : proxyConversationListenerRegistry.getAll()) {
			try {
				proxyConversationListener.onProxyConversationCompleted(proxyConversation);
			} catch (Exception e) {
				logger.debug("onProxyConversationCompleted failed on " + proxyConversationListener.getClass().getName(), e);
			}
		}

	}
}
