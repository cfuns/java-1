package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.proxy.api.ProxyConversation;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ProxyConversationNotifier {

	private final Logger logger;

	private final ProxyConversationListenerRegistry proxyConversationListenerRegistry;

	@Inject
	public ProxyConversationNotifier(final Logger logger, final ProxyConversationListenerRegistry proxyConversationListenerRegistry) {
		this.logger = logger;
		this.proxyConversationListenerRegistry = proxyConversationListenerRegistry;
	}

	public void onProxyConversationCompleted(final ProxyConversation proxyConversation) {
		for (final ProxyConversationListener proxyConversationListener : proxyConversationListenerRegistry.getAll()) {
			try {
				logger.trace("notify  " + proxyConversationListener.getClass().getSimpleName() + " started");
				proxyConversationListener.onProxyConversationCompleted(proxyConversation);
				logger.trace("notify " + proxyConversationListener.getClass().getSimpleName() + " finished");
			} catch (Exception e) {
				logger.debug("onProxyConversationCompleted failed on " + proxyConversationListener.getClass().getName(), e);
			}
		}

	}
}
