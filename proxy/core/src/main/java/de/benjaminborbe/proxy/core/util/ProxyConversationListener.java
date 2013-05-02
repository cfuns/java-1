package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.proxy.api.ProxyConversation;

public interface ProxyConversationListener {

	void onProxyConversationCompleted(ProxyConversation proxyConversation) throws Exception;
}
