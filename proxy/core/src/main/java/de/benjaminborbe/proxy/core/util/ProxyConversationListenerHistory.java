package de.benjaminborbe.proxy.core.util;

import javax.inject.Inject;

import org.slf4j.Logger;

import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.core.config.ProxyCoreConfig;
import de.benjaminborbe.proxy.core.dao.ProxyCoreConversationDao;

public class ProxyConversationListenerHistory implements ProxyConversationListener {

    private final Logger logger;

    private final ProxyCoreConversationDao proxyCoreConversationDao;

    private final ProxyCoreConfig proxyCoreConfig;

    @Inject
    public ProxyConversationListenerHistory(
            final Logger logger,
            final ProxyCoreConversationDao proxyCoreConversationDao,
            final ProxyCoreConfig proxyCoreConfig) {
        this.logger = logger;
        this.proxyCoreConversationDao = proxyCoreConversationDao;
        this.proxyCoreConfig = proxyCoreConfig;
    }

    @Override
    public void onProxyConversationCompleted(final ProxyConversation proxyConversation) {
        if (proxyCoreConfig.conversationHistory()) {
            logger.debug("add proxy request to history");
            proxyCoreConversationDao.add(proxyConversation);
        } else {
            logger.trace("skip");
        }
    }
}
