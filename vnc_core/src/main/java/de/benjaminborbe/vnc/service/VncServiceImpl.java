package de.benjaminborbe.vnc.service;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncKeyParseException;
import de.benjaminborbe.vnc.api.VncKeyParser;
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

	private final VncKeyParser vncKeyParser;

	@Inject
	public VncServiceImpl(
			final Logger logger,
			final VncConnector vncConnector,
			final VncAutoConnector vncAutoConnector,
			final VncStoreImageContentAction vncStoreImageContentAction,
			final VncKeyParser vncKeyParser) {
		this.logger = logger;
		this.vncConnector = vncConnector;
		this.vncAutoConnector = vncAutoConnector;
		this.vncStoreImageContentAction = vncStoreImageContentAction;
		this.vncKeyParser = vncKeyParser;
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
			logger.trace("getScreenContent");
			return vncConnector.getScreenContent();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void keyPress(final VncKey key) throws VncServiceException {
		try {
			logger.trace("keyPress - key: " + key);
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
			logger.trace("keyRelease - key: " + key);
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
			logger.trace("mouseLeftButtonPress");
			vncConnector.mouseLeftButtonPress();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void mouseLeftButtonRelease() throws VncServiceException {
		try {
			logger.trace("mouseLeftButtonRelease");
			vncConnector.mouseLeftButtonRelease();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void mouseMouse(final int x, final int y) throws VncServiceException {
		try {
			logger.trace("mouseMouse - x: " + x + " y: " + y);
			vncConnector.mouseMouse(x, y);
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void mouseMouse(final VncLocation location) throws VncServiceException {
		logger.trace("mouseMouse - x: " + location.getX() + " y: " + location.getY());
		mouseMouse(location.getX(), location.getY());
	}

	@Override
	public void storeImageContent() throws VncServiceException {
		try {
			logger.trace("storeImageContent");
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
	public void storeImageContent(final String name) throws VncServiceException {
		try {
			logger.trace("storeImageContent - name: " + name);
			vncStoreImageContentAction.storeImage(name);
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
			logger.trace("storeImageContent - vncPixels: " + vncPixels + " name: " + name);
			vncStoreImageContentAction.storeImage(vncPixels, name);
		}
		catch (final IOException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void keyType(final List<VncKey> keys) throws VncServiceException {
		logger.trace("keyType - keys: " + keys);
		for (final VncKey key : keys) {
			keyType(key);
		}
	}

	@Override
	public void keyType(final String keys) throws VncServiceException {
		logger.trace("keyType - keys: " + keys);
		try {
			for (final VncKey key : vncKeyParser.parseKeys(keys)) {
				keyPress(key);
				keyRelease(key);
			}
		}
		catch (final VncKeyParseException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void mouseLeftClick() throws VncServiceException {
		try {
			logger.debug("mouseLeftClick");
			vncConnector.mouseLeftClick();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public void mouseLeftClickDouble() throws VncServiceException {
		try {
			logger.debug("mouseLeftClickDouble");
			vncConnector.mouseLeftClickDouble();
		}
		catch (final VncConnectorException e) {
			throw new VncServiceException(e);
		}
	}

	@Override
	public VncKeyParser getVncKeyParser() {
		logger.trace("getVncKeyParser");
		return vncKeyParser;
	}

	@Override
	public void keyType(final VncKey key) throws VncServiceException {
		logger.trace("keyType - key: " + key);
		keyPress(key);
		keyRelease(key);
	}
}
