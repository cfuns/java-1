package de.benjaminborbe.vnc.connector;

import com.glavsoft.viewer.Viewer;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class VncConnector {

	private final Provider<Viewer> viewerProvider;

	@Inject
	public VncConnector(final Provider<Viewer> viewerProvider) {
		this.viewerProvider = viewerProvider;
	}

	public History getHistory() {
		return viewerProvider.get().getHistory();
	}

	public void connect() {
		viewerProvider.get().run();
	}

	public void diconnect() {
	}

	public void mouseClick() {
	}

	public void mouseMouse(final int x, final int y) {
	}

	public void keyRelease(final Key key) {
	}

	public void keyPress(final Key key) {
	}
}
