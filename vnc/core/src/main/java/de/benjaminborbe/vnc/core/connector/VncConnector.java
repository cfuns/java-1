package de.benjaminborbe.vnc.core.connector;

import com.glavsoft.exceptions.AuthenticationFailedException;
import com.glavsoft.exceptions.FatalException;
import com.glavsoft.exceptions.TransportException;
import com.glavsoft.exceptions.UnsupportedProtocolVersionException;
import com.glavsoft.exceptions.UnsupportedSecurityTypeException;
import com.glavsoft.rfb.client.ClientToServerMessage;
import com.glavsoft.rfb.client.KeyEventMessage;
import com.glavsoft.rfb.client.PointerEventMessage;
import com.glavsoft.viewer.Viewer;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncScreenContent;
import org.slf4j.Logger;

import java.io.IOException;

@Singleton
public class VncConnector {

	private final Provider<Viewer> viewerProvider;

	private final VncKeyTranslater vncKeyTranslater;

	private final Provider<VncScreenContent> vncScreenContentProvider;

	private final VncPointerLocation vncPointerLocation;

	private boolean connected;

	private final Logger logger;

	private Viewer viewer;

	@Inject
	public VncConnector(
		final Logger logger,
		final Provider<Viewer> viewerProvider,
		final VncKeyTranslater vncKeyTranslater,
		final Provider<VncScreenContent> vncScreenContentProvider,
		final VncPointerLocation vncPointerLocation) {
		this.logger = logger;
		this.viewerProvider = viewerProvider;
		this.vncKeyTranslater = vncKeyTranslater;
		this.vncScreenContentProvider = vncScreenContentProvider;
		this.vncPointerLocation = vncPointerLocation;
	}

	public VncHistory getHistory() {
		return getViewer().getHistory();
	}

	public synchronized void connect() throws VncConnectorException {

		logger.debug("try connect");
		if (!isConnected()) {
			try {
				viewer = viewerProvider.get();
				viewer.connect();
				connected = true;
				logger.debug("connect complete");
			} catch (final UnsupportedProtocolVersionException | FatalException | TransportException | AuthenticationFailedException | UnsupportedSecurityTypeException e) {
				logger.debug(e.getClass().getName(), e);
				throw new VncConnectorException(e.getClass().getName(), e);
			} catch (final IOException e) {
				logger.debug(e.getClass().getName(), e);
				throw new VncConnectorException(e.getClass().getName(), e);
			}
		} else {
			throw new VncConnectorException("already connected!");
		}
	}

	public synchronized void disconnect() throws VncConnectorException {
		logger.debug("try disconnect");
		if (isConnected()) {
			viewer.disconnect();
			viewer = null;
			connected = false;
			logger.debug("disconnect complete");
		} else {
			logger.warn("already disconnected");
		}
	}

	public void mouseMouse(final int x, final int y) throws VncConnectorException {
		expectConnected();
		final byte buttonMask = 0;
		sendMessage(new PointerEventMessage(logger, vncPointerLocation, buttonMask, (short) x, (short) y));
	}

	public void keyRelease(final VncKey vncKey) throws VncConnectorException, VncKeyTranslaterException {
		sendMessage(new KeyEventMessage(logger, vncKeyTranslater.translate(vncKey), false));
	}

	public void keyPress(final VncKey vncKey) throws VncConnectorException, VncKeyTranslaterException {
		sendMessage(new KeyEventMessage(logger, vncKeyTranslater.translate(vncKey), true));
	}

	public void mouseLeftButtonPress() throws VncConnectorException {
		final byte buttonMask = 1;
		sendMessage(new PointerEventMessage(logger, vncPointerLocation, buttonMask, (short) vncPointerLocation.getX(), (short) vncPointerLocation.getY()));
	}

	public void mouseLeftButtonRelease() throws VncConnectorException {
		final byte buttonMask = 0;
		sendMessage(new PointerEventMessage(logger, vncPointerLocation, buttonMask, (short) vncPointerLocation.getX(), (short) vncPointerLocation.getY()));
	}

	public boolean isConnected() {
		return connected;
	}

	private void sendMessage(final ClientToServerMessage message) throws VncConnectorException {
		expectConnected();
		getViewer().sendMessage(message);
	}

	public Viewer getViewer() {
		return viewer;
	}

	private void expectConnected() throws VncConnectorException {
		if (!isConnected()) {
			throw new VncConnectorException("connect first");
		}
	}

	public VncScreenContent getScreenContent() throws VncConnectorException {
		expectConnected();
		return vncScreenContentProvider.get();
	}

	public void mouseLeftClickDouble() throws VncConnectorException {
		mouseLeftButtonPress();
		mouseLeftButtonPress();
	}

	public void mouseLeftClick() throws VncConnectorException {
		mouseLeftButtonPress();
	}

}
