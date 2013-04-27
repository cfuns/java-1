package de.benjaminborbe.vnc.core.util;

import de.benjaminborbe.vnc.core.connector.VncConnector;
import de.benjaminborbe.vnc.core.connector.VncConnectorException;
import org.slf4j.Logger;

import javax.inject.Inject;

public class VncAutoConnector {

	private int connectCounter = 0;

	private final VncConnector vncConnector;

	private final Logger logger;

	@Inject
	public VncAutoConnector(final Logger logger, final VncConnector vncConnector) {
		this.logger = logger;
		this.vncConnector = vncConnector;
	}

	public synchronized void connect() throws VncConnectorException {
		logger.debug("try connect - counter = " + connectCounter);
		if (connectCounter == 0) {
			vncConnector.connect();
			logger.debug("connect called");
		} else {
			logger.debug("connect skipped");
		}
		connectCounter++;
	}

	public synchronized void disconnect() throws VncConnectorException {
		logger.debug("try disconnect - counter = " + connectCounter);
		if (connectCounter == 1) {
			logger.debug("disconnect called");
			vncConnector.disconnect();
		} else {
			logger.debug("disconnect skipped");
		}
		if (connectCounter > 0) {
			connectCounter--;
		}
	}

	public void disconnectForce() throws VncConnectorException {
		logger.debug("disconnect force");
		connectCounter = 0;
		vncConnector.disconnect();
	}
}
