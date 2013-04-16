package com.glavsoft.viewer;

import com.glavsoft.core.SettingsChangedEvent;
import com.glavsoft.drawing.Renderer;
import com.glavsoft.exceptions.AuthenticationFailedException;
import com.glavsoft.exceptions.FatalException;
import com.glavsoft.exceptions.TransportException;
import com.glavsoft.exceptions.UnsupportedProtocolVersionException;
import com.glavsoft.exceptions.UnsupportedSecurityTypeException;
import com.glavsoft.rfb.ClipboardController;
import com.glavsoft.rfb.IChangeSettingsListener;
import com.glavsoft.rfb.IPasswordRetriever;
import com.glavsoft.rfb.IRepaintController;
import com.glavsoft.rfb.IRfbSessionListener;
import com.glavsoft.rfb.client.ClientToServerMessage;
import com.glavsoft.rfb.encoding.PixelFormat;
import com.glavsoft.rfb.encoding.decoder.FramebufferUpdateRectangle;
import com.glavsoft.rfb.protocol.MessageQueue;
import com.glavsoft.rfb.protocol.Protocol;
import com.glavsoft.rfb.protocol.ProtocolSettings;
import com.glavsoft.transport.Reader;
import com.glavsoft.transport.Writer;
import com.glavsoft.viewer.swing.ConnectionParams;
import com.glavsoft.viewer.swing.ParametersHandler;
import com.glavsoft.viewer.swing.RendererImpl;
import com.glavsoft.viewer.swing.UiSettings;
import com.google.inject.Inject;
import de.benjaminborbe.tools.thread.Assert;
import de.benjaminborbe.tools.thread.ThreadUtil;
import de.benjaminborbe.vnc.core.VncConstants;
import de.benjaminborbe.vnc.core.config.VncConfig;
import de.benjaminborbe.vnc.core.connector.VncHistory;
import org.slf4j.Logger;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class ViewerHeadless implements Viewer, IRfbSessionListener, WindowListener, IChangeSettingsListener {

	private final class MyIPasswordRetriever implements IPasswordRetriever {

		@Override
		public String getPassword() {
			return vncConfig.getPassword();
		}
	}

	private final class MyIRepaintController implements IRepaintController {

		@Override
		public void settingsChanged(final SettingsChangedEvent event) {
		}

		@Override
		public void repaintBitmap(final FramebufferUpdateRectangle rect) {
		}

		@Override
		public void repaintBitmap(final int x, final int y, final int width, final int height) {
		}

		@Override
		public void repaintCursor() {
		}

		@Override
		public void updateCursorPosition(final short x, final short y) {
		}

		@Override
		public Renderer createRenderer(final Reader reader, final int width, final int height, final PixelFormat pixelFormat) {
			return new RendererImpl(logger, reader, width, height, pixelFormat);
		}

		@Override
		public void setPixelFormat(final PixelFormat pixelFormat) {
		}
	}

	private final class MyClipboardController implements ClipboardController {

		@Override
		public void settingsChanged(final SettingsChangedEvent event) {
		}

		@Override
		public void updateSystemClipboard(final byte[] bytes) {
		}

		@Override
		public void setEnabled(final boolean enable) {
		}

		@Override
		public String getRenewedClipboardText() {
			return null;
		}

		@Override
		public String getClipboardText() {
			return null;
		}
	}

	private static final int CONNECT_TIMEOUT = 1000;

	private boolean isZoomToFitSelected;

	private final ConnectionParams connectionParams;

	private Socket workingSocket;

	private Protocol workingProtocol;

	boolean isSeparateFrame = true;

	private final ProtocolSettings settings;

	private final UiSettings uiSettings;

	private volatile boolean isStoppingProcess;

	private List<JComponent> kbdButtons;

	private final VncHistory history;

	private final Logger logger;

	private final VncConfig vncConfig;

	private final ThreadUtil threadUtil;

	@Inject
	public ViewerHeadless(final Logger logger, final VncConfig vncConfig, final VncHistory history, final ThreadUtil threadUtil) {
		this.logger = logger;
		this.vncConfig = vncConfig;
		this.history = history;
		this.threadUtil = threadUtil;
		this.connectionParams = new ConnectionParams(vncConfig.getHostname(), vncConfig.getPort());
		settings = ProtocolSettings.getDefaultSettings(logger);
		uiSettings = new UiSettings();
	}

	@Override
	public void rfbSessionStopped(final String reason) {
		if (isStoppingProcess)
			return;
		cleanUpUISessionAndConnection();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
			}
		});
		// start new session
		// SwingUtilities.invokeLater(this);
	}

	private synchronized void cleanUpUISessionAndConnection() {
		isStoppingProcess = true;
		if (workingSocket != null && workingSocket.isConnected()) {
			try {
				workingSocket.close();
			} catch (final IOException e) { /* nop */
			}
			workingSocket = null;
		}

		isStoppingProcess = false;
	}

	@Override
	public void windowClosing(final WindowEvent e) {
		if (e != null && e.getComponent() != null) {
			e.getWindow().setVisible(false);
		}
		closeApp();
	}

	/**
	 * Closes App(lication) or stops App(let).
	 */
	private void closeApp() {
		if (workingProtocol != null) {
			workingProtocol.cleanUpSession();
		}
		cleanUpUISessionAndConnection();

	}

	public void init() {
		isSeparateFrame = ParametersHandler.isSeparateFrame;
		// SwingUtilities.invokeLater(this);
	}

	@Override
	public void connect() throws UnsupportedProtocolVersionException, UnsupportedSecurityTypeException, AuthenticationFailedException, TransportException, FatalException,
		UnknownHostException, IOException {
		logger.debug("connect - started");
		workingSocket = connectToHost(connectionParams, settings);
		workingSocket.setTcpNoDelay(true); // disable Nagle algorithm

		logger.debug("create reader");
		final Reader reader = new Reader(workingSocket.getInputStream());
		logger.debug("create writer");
		final Writer writer = new Writer(workingSocket.getOutputStream());

		final IPasswordRetriever pw = new MyIPasswordRetriever();
		workingProtocol = new Protocol(logger, history, reader, writer, pw, settings);
		workingProtocol.handshake();

		final IRepaintController surface = new MyIRepaintController();
		final ClipboardController clipboardController = new MyClipboardController();
		workingProtocol.startNormalHandling(this, surface, clipboardController);
		logger.debug("connect - complete");
	}

	private Socket connectToHost(final ConnectionParams connectionParams, final ProtocolSettings settings) throws UnknownHostException, IOException {
		final String host = connectionParams.hostName;
		final int port = connectionParams.getPortNumber();
		final Socket sock = new Socket();
		sock.connect(new InetSocketAddress(host, port), CONNECT_TIMEOUT);
		return sock;
	}

	@Override
	public void settingsChanged(final SettingsChangedEvent e) {
		final ProtocolSettings settings = (ProtocolSettings) e.getSource();
		setEnabledKbdButtons(!settings.isViewOnly());
	}

	private void setEnabledKbdButtons(final boolean enabled) {
		if (kbdButtons != null) {
			for (final JComponent b : kbdButtons) {
				b.setEnabled(enabled);
			}
		}
	}

	@Override
	public void windowOpened(final WindowEvent e) { /* nop */
	}

	@Override
	public void windowClosed(final WindowEvent e) { /* nop */
	}

	@Override
	public void windowIconified(final WindowEvent e) { /* nop */
	}

	@Override
	public void windowDeiconified(final WindowEvent e) { /* nop */
	}

	@Override
	public void windowActivated(final WindowEvent e) { /* nop */
	}

	@Override
	public void windowDeactivated(final WindowEvent e) { /* nop */
	}

	@Override
	public VncHistory getHistory() {
		return history;
	}

	@Override
	public Renderer getRenderer() {
		return workingProtocol.getRenderer();
	}

	@Override
	public void sendMessage(final ClientToServerMessage message) {
		workingProtocol.sendMessage(message);
	}

	@Override
	public void disconnect() {
		logger.debug("disconnect - start");
		if (workingProtocol != null) {
			try {
				threadUtil.wait(VncConstants.DISCONNECT_TIMEOUT, new Assert() {

					@Override
					public boolean calc() {
						final Protocol p = workingProtocol;
						if (p == null)
							return true;
						final MessageQueue m = p.getMessageQueue();
						if (m == null)
							return true;
						return m.isEmpty();
					}
				});
			} catch (final InterruptedException e) {
			}

			workingProtocol.cleanUpSession();
		}
		cleanUpUISessionAndConnection();
		logger.debug("disconnect - done");
	}

	public Protocol getWorkingProtocol() {
		return workingProtocol;
	}

	public boolean isZoomToFitSelected() {
		return isZoomToFitSelected;
	}

	public UiSettings getUiSettings() {
		return uiSettings;
	}

	public void setZoomToFitSelected(final boolean zoomToFitSelected) {
		isZoomToFitSelected = zoomToFitSelected;
	}

}
