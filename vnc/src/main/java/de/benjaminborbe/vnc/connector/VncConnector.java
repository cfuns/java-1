package de.benjaminborbe.vnc.connector;

import com.glavsoft.viewer.Viewer;
import com.google.inject.Inject;

public class VncConnector {

	private final Viewer viewer;

	@Inject
	public VncConnector(final Viewer viewer) {
		this.viewer = viewer;
	}

	public History getHistory() {
		return viewer.getHistory();
	}

	public void connect() {
		viewer.run();
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
