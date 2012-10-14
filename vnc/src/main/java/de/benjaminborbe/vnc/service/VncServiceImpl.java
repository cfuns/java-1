package de.benjaminborbe.vnc.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.connector.VncConnector;

@Singleton
public class VncServiceImpl implements VncService {

	private final Logger logger;

	private final VncConnector vncConnector;

	private final VncScreenContent vncScreenContent;

	@Inject
	public VncServiceImpl(final Logger logger, final VncConnector vncConnector, final VncScreenContent vncScreenContent) {
		this.logger = logger;
		this.vncConnector = vncConnector;
		this.vncScreenContent = vncScreenContent;
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

	@Override
	public VncScreenContent getScreenContent() {
		return vncScreenContent;
	}

	@Override
	public void keyPress(final VncKey key) {
		vncConnector.keyPress(key);
	}

	@Override
	public void keyRelease(final VncKey key) {
		vncConnector.keyRelease(key);
	}

	@Override
	public void mouseLeftButtonPress() {
		vncConnector.mouseLeftButtonPress();
	}

	@Override
	public void mouseLeftButtonRelease() {
		vncConnector.mouseLeftButtonRelease();
	}

	@Override
	public void mouseMouse(final int x, final int y) {
		vncConnector.mouseMouse(x, y);
	}
}
