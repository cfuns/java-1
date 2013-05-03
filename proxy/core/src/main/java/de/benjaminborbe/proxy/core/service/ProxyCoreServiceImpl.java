package de.benjaminborbe.proxy.core.service;

import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.api.ProxyConversationIdentifier;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;
import de.benjaminborbe.proxy.core.dao.ProxyCoreConversationDao;
import de.benjaminborbe.proxy.core.util.ProxyServer;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class ProxyCoreServiceImpl implements ProxyService {

	private final Logger logger;

	private final ProxyServer proxyServer;

	private final ProxyCoreConversationDao proxyCoreConversationDao;

	@Inject
	public ProxyCoreServiceImpl(final Logger logger, final ProxyServer proxyServer, final ProxyCoreConversationDao proxyCoreConversationDao) {
		this.logger = logger;
		this.proxyServer = proxyServer;
		this.proxyCoreConversationDao = proxyCoreConversationDao;
	}

	@Override
	public void start() throws ProxyServiceException {
		logger.info("start");
		proxyServer.start();
	}

	@Override
	public void stop() throws ProxyServiceException {
		logger.info("stop");
		proxyServer.stop();
	}

	@Override
	public Collection<ProxyConversationIdentifier> getConversations() throws ProxyServiceException {
		logger.debug("getConversations");
		return proxyCoreConversationDao.getProxyConversationIdentifiers();
	}

	@Override
	public ProxyConversation getProxyConversation(final ProxyConversationIdentifier proxyConversationIdentifier) throws ProxyServiceException {
		logger.debug("getProxyConversation");
		return proxyCoreConversationDao.getProxyConversation(proxyConversationIdentifier);
	}

	@Override
	public ProxyConversationIdentifier createProxyConversationIdentifier(final String id) throws ProxyServiceException {
		logger.debug("createProxyConversationIdentifier");
		if (id != null && !id.trim().isEmpty()) {
			return new ProxyConversationIdentifier(id);
		} else {
			return null;
		}
	}

	@Override
	public Integer getServerPort() {
		logger.debug("getServerPort");
		return proxyServer.getPort();
	}

}
