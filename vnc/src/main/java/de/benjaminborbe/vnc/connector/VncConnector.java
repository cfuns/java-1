package de.benjaminborbe.vnc.connector;

import com.glavsoft.rfb.client.ClientToServerMessage;
import com.glavsoft.rfb.client.KeyEventMessage;
import com.glavsoft.rfb.client.PointerEventMessage;
import com.glavsoft.viewer.Viewer;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncKey;
import de.benjaminborbe.vnc.api.VncScreenContent;

@Singleton
public class VncConnector {

	private final Provider<Viewer> viewerProvider;

	private final VncKeyTranslater vncKeyTranslater;

	private final Provider<VncScreenContent> vncScreenContentProvider;

	private final VncPointerLocation vncPointerLocation;

	private boolean connected;

	@Inject
	public VncConnector(
			final Provider<Viewer> viewerProvider,
			final VncKeyTranslater vncKeyTranslater,
			final Provider<VncScreenContent> vncScreenContentProvider,
			final VncPointerLocation vncPointerLocation) {
		this.viewerProvider = viewerProvider;
		this.vncKeyTranslater = vncKeyTranslater;
		this.vncScreenContentProvider = vncScreenContentProvider;
		this.vncPointerLocation = vncPointerLocation;
	}

	public VncHistory getHistory() {
		return getViewer().getHistory();
	}

	public synchronized void connect() {
		if (!isConnected()) {
			getViewer().run();
			connected = true;
		}
	}

	public synchronized void diconnect() {
		if (isConnected()) {
			connected = false;
		}
	}

	public void mouseMouse(final int x, final int y) throws VncConnectorException {
		expectConnected();
		final byte buttonMask = 0;
		sendMessage(new PointerEventMessage(vncPointerLocation, buttonMask, (short) x, (short) y));

	}

	public void keyRelease(final VncKey vncKey) throws VncConnectorException, VncKeyTranslaterException {
		sendMessage(new KeyEventMessage(vncKeyTranslater.translate(vncKey), false));
	}

	public void keyPress(final VncKey vncKey) throws VncConnectorException, VncKeyTranslaterException {
		sendMessage(new KeyEventMessage(vncKeyTranslater.translate(vncKey), true));
	}

	public void mouseLeftButtonPress() throws VncConnectorException {

	}

	public void mouseLeftButtonRelease() throws VncConnectorException {

	}

	public boolean isConnected() {
		return connected;
	}

	private void sendMessage(final ClientToServerMessage message) throws VncConnectorException {
		expectConnected();
		getViewer().sendMessage(message);
	}

	private Viewer getViewer() {
		return viewerProvider.get();
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

}
