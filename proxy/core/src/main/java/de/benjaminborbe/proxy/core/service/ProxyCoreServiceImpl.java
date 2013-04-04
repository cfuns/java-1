package de.benjaminborbe.proxy.core.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.benjaminborbe.proxy.api.ProxyService;
import de.benjaminborbe.proxy.api.ProxyServiceException;
import de.benjaminborbe.proxy.core.util.ProxySocket;
import org.slf4j.Logger;

@Singleton
public class ProxyCoreServiceImpl implements ProxyService {

	private final Logger logger;

	private final ProxySocket proxySocket;

	@Inject
	public ProxyCoreServiceImpl(final Logger logger, final ProxySocket proxySocket) {
		this.logger = logger;
		this.proxySocket = proxySocket;
	}

	@Override
	public long calc(final long value) {
		logger.trace("execute");
		return value * 2;
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

}
