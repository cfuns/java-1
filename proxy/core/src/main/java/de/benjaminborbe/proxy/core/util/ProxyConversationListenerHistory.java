package de.benjaminborbe.proxy.core.util;

import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.core.dao.ProxyCoreConversationDao;
import org.slf4j.Logger;

import javax.inject.Inject;

public class ProxyConversationListenerHistory implements ProxyConversationListener {

	private final Logger logger;

	private final ProxyCoreConversationDao proxyCoreConversationDao;

	@Inject
	public ProxyConversationListenerHistory(final Logger logger, final ProxyCoreConversationDao proxyCoreConversationDao) {
		this.logger = logger;
		this.proxyCoreConversationDao = proxyCoreConversationDao;
	}

	@Override
	public void onProxyConversationCompleted(final ProxyConversation proxyConversation) {
		logger.debug("add proxy request to history");
		proxyCoreConversationDao.add(proxyConversation);
	}
}
