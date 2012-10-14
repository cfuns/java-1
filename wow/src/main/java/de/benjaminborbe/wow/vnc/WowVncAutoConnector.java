package de.benjaminborbe.wow.vnc;

import com.google.inject.Inject;

import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;

public class WowVncAutoConnector {

	private final VncService vncService;

	private int connectCounter = 0;

	@Inject
	public WowVncAutoConnector(final VncService vncService) {
		this.vncService = vncService;
	}

	public synchronized void connect() throws VncServiceException {
		if (connectCounter == 0) {
			vncService.connect();
		}
		connectCounter++;
	}

	public synchronized void disconnect() throws VncServiceException {
		if (connectCounter == 1) {
			vncService.disconnect();
		}
		connectCounter--;
	}
}
