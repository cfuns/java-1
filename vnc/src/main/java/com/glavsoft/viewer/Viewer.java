package com.glavsoft.viewer;

import com.glavsoft.drawing.Renderer;
import com.glavsoft.rfb.client.ClientToServerMessage;

import de.benjaminborbe.vnc.connector.VncHistory;

public interface Viewer {

	VncHistory getHistory();

	void run();

	void sendMessage(ClientToServerMessage message);

	Renderer getRenderer();

}
