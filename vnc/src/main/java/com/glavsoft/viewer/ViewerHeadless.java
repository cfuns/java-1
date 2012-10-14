package com.glavsoft.viewer;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import org.slf4j.Logger;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

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
import com.glavsoft.rfb.protocol.Protocol;
import com.glavsoft.rfb.protocol.ProtocolSettings;
import com.glavsoft.transport.Reader;
import com.glavsoft.transport.Writer;
import com.glavsoft.viewer.swing.ConnectionParams;
import com.glavsoft.viewer.swing.ParametersHandler;
import com.glavsoft.viewer.swing.RendererImpl;
import com.glavsoft.viewer.swing.UiSettings;
import com.google.inject.Inject;
import de.benjaminborbe.vnc.config.VncConfig;
import de.benjaminborbe.vnc.connector.VncHistory;

public class ViewerHeadless implements Viewer, Runnable, IRfbSessionListener, WindowListener, IChangeSettingsListener {

	private boolean isZoomToFitSelected;

	private final ConnectionParams connectionParams;

	private Socket workingSocket;

	private Protocol workingProtocol;

	boolean isSeparateFrame = true;

	private final ProtocolSettings settings;

	private final UiSettings uiSettings;

	private boolean tryAgain;

	private volatile boolean isStoppingProcess;

	private List<JComponent> kbdButtons;

	private final VncHistory history;

	private final Logger logger;

	private final VncConfig vncConfig;

	@Inject
	public ViewerHeadless(final Logger logger, final VncConfig vncConfig, final VncHistory history) {
		this.logger = logger;
		this.vncConfig = vncConfig;
		this.history = history;
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
		SwingUtilities.invokeLater(this);
	}

	private synchronized void cleanUpUISessionAndConnection() {
		isStoppingProcess = true;
		if (workingSocket != null && workingSocket.isConnected()) {
			try {
				workingSocket.close();
			}
			catch (final IOException e) { /* nop */
			}
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
		tryAgain = false;

	}

	public void init() {
		isSeparateFrame = ParametersHandler.isSeparateFrame;
		SwingUtilities.invokeLater(this);
	}

	@Override
	public void run() {
		tryAgain = true;
		while (tryAgain) {

			try {
				workingSocket = connectToHost(connectionParams, settings);
				workingSocket.setTcpNoDelay(true); // disable Nagle algorithm
				final Reader reader = new Reader(workingSocket.getInputStream());
				final Writer writer = new Writer(workingSocket.getOutputStream());

				final IPasswordRetriever pw = new IPasswordRetriever() {

					@Override
					public String getPassword() {
						return vncConfig.getPassword();
					}
				};
				workingProtocol = new Protocol(logger, history, reader, writer, pw, settings);
				workingProtocol.handshake();

				// final ClipboardControllerImpl clipboardController = new
				// ClipboardControllerImpl(workingProtocol, settings.getRemoteCharsetName());
				// clipboardController.setEnabled(settings.isAllowClipboardTransfer());
				// settings.addListener(clipboardController);
				// settings.addListener(this);
				// uiSettings.addListener(surface);
				// connectionManager.setContainerFrame(containerFrame);
				// updateFrameTitle();

				final IRepaintController surface = new IRepaintController() {

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
						return new RendererImpl(reader, width, height, pixelFormat);
					}

					@Override
					public void setPixelFormat(final PixelFormat pixelFormat) {
					}
				};
				final ClipboardController clipboardController = new ClipboardController() {

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
				};
				workingProtocol.startNormalHandling(this, surface, clipboardController);
				tryAgain = false;
			}
			catch (final UnsupportedProtocolVersionException e) {
				logger.debug(e.getMessage());
			}
			catch (final UnsupportedSecurityTypeException e) {
				logger.debug(e.getMessage());
			}
			catch (final AuthenticationFailedException e) {
				logger.debug(e.getMessage());
			}
			catch (final TransportException e) {
				logger.debug(e.getMessage());
			}
			catch (final IOException e) {
				logger.debug(e.getMessage());
			}
			catch (final FatalException e) {
				logger.debug(e.getMessage());
			}
		}
	}

	private Socket connectToHost(final ConnectionParams connectionParams, final ProtocolSettings settings) throws UnknownHostException, IOException {
		final String host = connectionParams.hostName;
		final int port = connectionParams.getPortNumber();
		return new Socket(host, port);
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
	public void connect() {
		run();
	}

	@Override
	public void disconnect() {
		if (workingProtocol != null) {
			workingProtocol.cleanUpSession();
		}
		cleanUpUISessionAndConnection();
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
