package de.benjaminborbe.vnc.service;

import java.io.IOException;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.api.VncService;
import de.benjaminborbe.vnc.api.VncServiceException;
import de.benjaminborbe.vnc.connector.VncConnector;
import de.benjaminborbe.vnc.connector.VncConnectorException;
import de.benjaminborbe.vnc.connector.VncKeyTranslaterException;
import de.benjaminborbe.vnc.util.VncAutoConnector;

@Singleton
public class VncServiceImpl implements VncService {

	private final Logger logger;

	private final VncConnector vncConnector;

	private final VncAutoConnector vncAutoConnector;

	private final VncStoreImageContentAction vncStoreImageContentAction;

	@Inject
	public VncServiceImpl(final Logger logger, final VncConnector vncConnector, final VncAutoConnector vncAutoConnector, final VncStoreImageContentAction vncStoreImageContentAction) {
		this.logger = logger;
		this.vncConnector = vncConnector;
		this.vncAutoConnector = vncAutoConnector;
		this.vncStoreImageContentAction = vncStoreImageContentAction;
	}

	@Override
	public void connect() throws VncServiceException {
		try {
			logger.trace("connect");
			vncAutoConnector.connect();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void disconnect() throws VncServiceException {
		try {
			logger.trace("disconnect");
			vncAutoConnector.disconnect();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void disconnectForce() throws VncServiceException {
		try {
			logger.debug("disconnectForce");
			vncAutoConnector.disconnectForce();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
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

	@Override
	public void mouseMouse(final VncLocation location) throws VncServiceException {
		mouseMouse(location.getX(), location.getY());
	}

	@Override
	public void storeImageContent() throws VncServiceException {
		try {
			vncStoreImageContentAction.storeImage();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
		catch (final IOException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void storeVncPixels(final VncPixels vncPixels, final String name) throws VncServiceException {
		try {
			vncStoreImageContentAction.storeImage(vncPixels, name);
		}
		catch (final IOException e) {
			throw new VncServiceException(e);
		}
	}
}
