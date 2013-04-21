package de.benjaminborbe.proxy.core.service;

import de.benjaminborbe.proxy.api.ProxyConversation;
import de.benjaminborbe.proxy.api.ProxyConversationIdentifier;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;
import de.benjaminborbe.proxy.core.dao.ProxyCoreConversationDao;
import de.benjaminborbe.proxy.core.util.ProxySocket;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;

@Singleton
public class ProxyCoreServiceImpl implements ProxyService {

	private final Logger logger;

	private final ProxySocket proxySocket;

	private final ProxyCoreConversationDao proxyCoreConversationDao;

	@Inject
	public ProxyCoreServiceImpl(final Logger logger, final ProxySocket proxySocket, final ProxyCoreConversationDao proxyCoreConversationDao) {
		this.logger = logger;
		this.proxySocket = proxySocket;
		this.proxyCoreConversationDao = proxyCoreConversationDao;
	}

	@Override
	public void start() throws ProxyServiceException {
		logger.info("start");
		proxySocket.start();
	}

	@Override
	public void stop() throws ProxyServiceException {
		logger.info("stop");
		proxySocket.stop();
	}

	@Override
	public Collection<ProxyConversationIdentifier> getConversations() throws ProxyServiceException {
		return proxyCoreConversationDao.getProxyConversationIdentifiers();
	}

	@Override
	public ProxyConversation getProxyConversation(final ProxyConversationIdentifier proxyConversationIdentifier) throws ProxyServiceException {
		return proxyCoreConversationDao.getProxyConversation(proxyConversationIdentifier);
	}

	@Override
	public ProxyConversationIdentifier createProxyConversationIdentifier(final String id) throws ProxyServiceException {
		if (id != null && !id.trim().isEmpty()) {
			return new ProxyConversationIdentifier(id);
		} else {
			return null;
		}
	}

}
