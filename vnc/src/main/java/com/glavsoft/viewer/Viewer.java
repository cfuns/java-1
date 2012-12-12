package com.glavsoft.viewer;

import java.io.IOException;
import java.net.UnknownHostException;

import com.glavsoft.drawing.Renderer;
import com.glavsoft.exceptions.AuthenticationFailedException;
import com.glavsoft.exceptions.FatalException;
import com.glavsoft.exceptions.TransportException;
import com.glavsoft.exceptions.UnsupportedProtocolVersionException;
import com.glavsoft.exceptions.UnsupportedSecurityTypeException;
import com.glavsoft.rfb.client.ClientToServerMessage;

import de.benjaminborbe.vnc.connector.VncHistory;

public interface Viewer {

	VncHistory getHistory();

	void connect() throws UnsupportedProtocolVersionException, UnsupportedSecurityTypeException, AuthenticationFailedException, TransportException, FatalException,
			UnknownHostException, IOException;

	void disconnect();

	void sendMessage(ClientToServerMessage message);

	Renderer getRenderer();

}
