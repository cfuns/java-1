package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.core.dao.ProxyCoreConversationDao;

import javax.inject.Inject;

public class ProxyConversationListenerHistory implements ProxyConversationListener {

	private final ProxyCoreConversationDao proxyCoreConversationDao;

	@Inject
	public ProxyConversationListenerHistory(final ProxyCoreConversationDao proxyCoreConversationDao) {
		this.proxyCoreConversationDao = proxyCoreConversationDao;
	}

	@Override
	public void onProxyConversationCompleted(final ProxyConversation proxyConversation) {
		proxyCoreConversationDao.add(proxyConversation);
	}
}
