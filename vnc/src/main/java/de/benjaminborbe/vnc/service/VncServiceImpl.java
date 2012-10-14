package de.benjaminborbe.vnc.service;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.vnc.connector.VncConnector;
import de.benjaminborbe.vnc.connector.VncConnectorException;
import de.benjaminborbe.vnc.connector.VncKeyTranslaterException;

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
	public void connect() throws VncServiceException {
		logger.trace("connect");
		vncConnector.connect();
	}

	@Override
	public void disconnect() throws VncServiceException {
		logger.trace("disconnect");
		vncConnector.diconnect();
	}

	@Override
	public VncScreenContent getScreenContent() throws VncServiceException {
		try {
			return vncConnector.getScreenContent();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void keyPress(final VncKey key) throws VncServiceException {
		try {
			vncConnector.keyPress(key);
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
		catch (final VncKeyTranslaterException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void keyRelease(final VncKey key) throws VncServiceException {
		try {
			vncConnector.keyRelease(key);
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
		catch (final VncKeyTranslaterException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void mouseLeftButtonPress() throws VncServiceException {
		try {
			vncConnector.mouseLeftButtonPress();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void mouseLeftButtonRelease() throws VncServiceException {
		try {
			vncConnector.mouseLeftButtonRelease();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void mouseMouse(final int x, final int y) throws VncServiceException {
		try {
			vncConnector.mouseMouse(x, y);
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}
}
