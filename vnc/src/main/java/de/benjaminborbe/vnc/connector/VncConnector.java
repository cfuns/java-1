package de.benjaminborbe.vnc.connector;

import com.glavsoft.rfb.client.ClientToServerMessage;
import com.glavsoft.rfb.client.KeyEventMessage;
import com.glavsoft.viewer.Viewer;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import de.benjaminborbe.vnc.api.VncKey;

@Singleton
public class VncConnector {

	private final Provider<Viewer> viewerProvider;

	private final VncKeyTranslater vncKeyTranslater;

	@Inject
	public VncConnector(final Provider<Viewer> viewerProvider, final VncKeyTranslater vncKeyTranslater) {
		this.viewerProvider = viewerProvider;
		this.vncKeyTranslater = vncKeyTranslater;
	}

	private void sendMessage(final ClientToServerMessage message) {
		getViewer().sendMessage(message);
	}

	public VncHistory getHistory() {
		return getViewer().getHistory();
	}

	public void connect() {
		getViewer().run();
	}

	private Viewer getViewer() {
		return viewerProvider.get();
	}

	public void diconnect() {
	}

	public void mouseMouse(final int x, final int y) {
	}

	public void keyRelease(final VncKey vncKey) {
		sendMessage(new KeyEventMessage(vncKeyTranslater.translate(vncKey), false));
	}

	public void keyPress(final VncKey vncKey) {
		sendMessage(new KeyEventMessage(vncKeyTranslater.translate(vncKey), true));
	}

	public void mouseLeftButtonPress() {
	}

	public void mouseLeftButtonRelease() {
	}
}
