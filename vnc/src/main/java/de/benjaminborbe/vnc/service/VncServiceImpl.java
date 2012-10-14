package de.benjaminborbe.vnc.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.connector.VncConnector;

@Singleton
public class VncServiceImpl implements VncService {

	private final Logger logger;

	private final VncConnector vncConnector;

	@Inject
	public VncServiceImpl(final Logger logger, final VncConnector vncConnector) {
		this.logger = logger;
		this.vncConnector = vncConnector;
	}

	@Override
	public void connect() {
		logger.trace("connect");
		vncConnector.connect();
	}

	@Override
	public void disconnect() {
		logger.trace("disconnect");
		vncConnector.diconnect();
	}
}
