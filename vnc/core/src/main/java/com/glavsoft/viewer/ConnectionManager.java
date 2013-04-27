// Copyright (C) 2010, 2011, 2012 GlavSoft LLC.
// All rights reserved.
//
//-------------------------------------------------------------------------
// This file is part of the TightVNC software.  Please visit our Web site:
//
//                       http://www.tightvnc.com/
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License along
// with this program; if not, write to the Free Software Foundation, Inc.,
// 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
//-------------------------------------------------------------------------
//

package com.glavsoft.viewer;

import com.glavsoft.rfb.protocol.ProtocolSettings;
import com.glavsoft.viewer.swing.ConnectionParams;
import com.glavsoft.viewer.swing.Utils;
import com.glavsoft.viewer.swing.gui.ConnectionDialog;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ConnectionManager {

	private final Logger logger;

	private final WindowListener appWindowListener;

	private volatile boolean forceConnectionDialog;

	private JFrame containerFrame;

	public ConnectionManager(final Logger logger, final WindowListener appWindowListener) {
		this.logger = logger;
		this.appWindowListener = appWindowListener;
	}

	protected void showReconnectDialog(final String title, final String message) {
		final JOptionPane reconnectPane = new JOptionPane(message + "\nTry another connection?", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		final JDialog reconnectDialog = reconnectPane.createDialog(containerFrame, title);
		Utils.decorateDialog(reconnectDialog);
		reconnectDialog.setVisible(true);
		if (reconnectPane.getValue() == null || (Integer) reconnectPane.getValue() == JOptionPane.NO_OPTION) {
			appWindowListener.windowClosing(null);
		}
	}

	Socket connectToHost(final ConnectionParams connectionParams, final ProtocolSettings settings) {
		Socket socket = null;
		ConnectionDialog connectionDialog = null;
		boolean wasError = false;
		int port = 0;
		final boolean hasJsch = false;
		do {
			if (forceConnectionDialog || wasError || connectionParams.isHostNameEmpty()) {
				forceConnectionDialog = false;
				if (null == connectionDialog) {
					connectionDialog = new ConnectionDialog(logger, containerFrame, appWindowListener, connectionParams, settings, hasJsch);
				}
				connectionDialog.setVisible(true);
			}
			final String host;
			{
				host = connectionParams.hostName;
				port = connectionParams.getPortNumber();
			}
			logger.info("Connecting to host " + host + ":" + port + (connectionParams.useSsh() ? " (tunneled)" : ""));
			try {
				socket = new Socket(host, port);
				wasError = false;
			} catch (final UnknownHostException e) {
				logger.warn("Unknown host: " + connectionParams.hostName);
				showConnectionErrorDialog("Unknown host: '" + connectionParams.hostName + "'");
				wasError = true;
			} catch (final IOException e) {
				logger.warn("Couldn't connect to: " + connectionParams.hostName + ":" + connectionParams.getPortNumber() + ": " + e.getMessage());
				showConnectionErrorDialog("Couldn't connect to: '" + connectionParams.hostName + ":" + connectionParams.getPortNumber() + "'\n" + e.getMessage());
				wasError = true;
			}
		} while (connectionParams.isHostNameEmpty() || wasError);
		if (connectionDialog != null) {
			connectionDialog.dispose();
		}
		return socket;
	}

	public void showConnectionErrorDialog(final String message) {
		final JOptionPane errorPane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
		final JDialog errorDialog = errorPane.createDialog(containerFrame, "Connection error");
		Utils.decorateDialog(errorDialog);
		errorDialog.setVisible(true);
	}

	public void setContainerFrame(final JFrame containerFrame) {
		if (this.containerFrame != null && this.containerFrame != containerFrame) {
			this.containerFrame.dispose();
		}
		this.containerFrame = containerFrame;
	}

}
