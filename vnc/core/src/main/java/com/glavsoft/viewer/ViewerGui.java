package com.glavsoft.viewer;

import com.glavsoft.core.SettingsChangedEvent;
import com.glavsoft.drawing.Renderer;
import com.glavsoft.exceptions.AuthenticationFailedException;
import com.glavsoft.exceptions.FatalException;
import com.glavsoft.exceptions.TransportException;
import com.glavsoft.exceptions.UnsupportedProtocolVersionException;
import com.glavsoft.exceptions.UnsupportedSecurityTypeException;
import com.glavsoft.rfb.IChangeSettingsListener;
import com.glavsoft.rfb.IPasswordRetriever;
import com.glavsoft.rfb.IRfbSessionListener;
import com.glavsoft.rfb.client.ClientToServerMessage;
import com.glavsoft.rfb.client.KeyEventMessage;
import com.glavsoft.rfb.protocol.Protocol;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.rfb.protocol.ProtocolSettings;
import com.glavsoft.transport.Reader;
import com.glavsoft.transport.Writer;
import com.glavsoft.utils.Keymap;
import com.glavsoft.utils.Strings;
import com.glavsoft.viewer.swing.ClipboardControllerImpl;
import com.glavsoft.viewer.swing.ConnectionParams;
import com.glavsoft.viewer.swing.ModifierButtonEventListener;
import com.glavsoft.viewer.swing.ParametersHandler;
import com.glavsoft.viewer.swing.Surface;
import com.glavsoft.viewer.swing.UiSettings;
import com.glavsoft.viewer.swing.gui.OptionsDialog;
import com.glavsoft.viewer.swing.gui.PasswordDialog;
import de.benjaminborbe.vnc.core.config.VncConfig;
import de.benjaminborbe.vnc.core.connector.VncHistory;
import de.benjaminborbe.vnc.core.connector.VncPointerLocation;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class ViewerGui extends JApplet implements Viewer, Runnable, IRfbSessionListener, WindowListener, IChangeSettingsListener {

	private static final long serialVersionUID = 7875810628566999258L;

	private boolean isZoomToFitSelected;

	private boolean forceReconnection;

	private String reconnectionReason;

	private ContainerManager containerManager;

	public Protocol getWorkingProtocol() {
		return workingProtocol;
	}

	public boolean isZoomToFitSelected() {
		return isZoomToFitSelected;
	}

	public Surface getSurface() {
		return surface;
	}

	public UiSettings getUiSettings() {
		return uiSettings;
	}

	public void setZoomToFitSelected(final boolean zoomToFitSelected) {
		isZoomToFitSelected = zoomToFitSelected;
	}

	/**
	 * Ask user for password if needed
	 */
	private class PasswordChooser implements IPasswordRetriever {

		private final String passwordPredefined;

		private final ConnectionParams connectionParams;

		private PasswordDialog passwordDialog;

		private final JFrame owner;

		private final WindowListener onClose;

		private PasswordChooser(final String passwordPredefined, final ConnectionParams connectionParams, final JFrame owner, final WindowListener onClose) {
			this.passwordPredefined = passwordPredefined;
			this.connectionParams = connectionParams;
			this.owner = owner;
			this.onClose = onClose;
		}

		@Override
		public String getPassword() {
			return Strings.isTrimmedEmpty(passwordPredefined) ? getPasswordFromGUI() : passwordPredefined;
		}

		private String getPasswordFromGUI() {
			if (null == passwordDialog) {
				passwordDialog = new PasswordDialog(owner, onClose);
			}
			passwordDialog.setServerHostName(connectionParams.hostName + ":" + connectionParams.getPortNumber());
			passwordDialog.toFront();
			passwordDialog.setVisible(true);
			return passwordDialog.getPassword();
		}
	}

	private final ConnectionParams connectionParams;

	private String passwordFromParams;

	private Socket workingSocket;

	private Protocol workingProtocol;

	private JFrame containerFrame;

	boolean isSeparateFrame = true;

	boolean showControls = true;

	private Surface surface;

	private final ProtocolSettings settings;

	private final UiSettings uiSettings;

	private boolean tryAgain;

	private final boolean isAppletStopped = false;

	private volatile boolean isStoppingProcess;

	private List<JComponent> kbdButtons;

	private final VncHistory history;

	private final Logger logger;

	private final VncPointerLocation vncPointerLocation;

	@Inject
	public ViewerGui(final Logger logger, final VncPointerLocation vncPointerLocation, final VncConfig vncConfig, final VncHistory history) {
		this.logger = logger;
		this.vncPointerLocation = vncPointerLocation;
		this.history = history;
		this.connectionParams = new ConnectionParams(vncConfig.getHostname(), vncConfig.getPort());
		this.passwordFromParams = vncConfig.getPassword();
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
				forceReconnection = true;
				reconnectionReason = reason;
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
			} catch (final IOException e) { /* nop */
			}
		}
		if (containerFrame != null) {
			containerFrame.dispose();
			containerFrame = null;
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

	@Override
	public void paint(final Graphics g) {
		if (!isAppletStopped) {
			super.paint(g);
		} else {
			getContentPane().removeAll();
			g.clearRect(0, 0, getWidth(), getHeight());
			g.drawString("Disconnected", 10, 20);
		}
	}

	@Override
	public void destroy() {
		closeApp();
		super.destroy();
	}

	@Override
	public void init() {
		ParametersHandler.completeSettingsFromApplet(this, connectionParams, settings, uiSettings);
		showControls = ParametersHandler.showControls;
		isSeparateFrame = ParametersHandler.isSeparateFrame;
		passwordFromParams = getParameter(ParametersHandler.ARG_PASSWORD);

		repaint();
		SwingUtilities.invokeLater(this);
	}

	@Override
	public void start() {
		setSurfaceToHandleKbdFocus();
		super.start();
	}

	@Override
	public void run() {
		final ConnectionManager connectionManager = new ConnectionManager(logger, this);

		if (forceReconnection) {
			connectionManager.showReconnectDialog("Connection lost", reconnectionReason);
			forceReconnection = false;
		}
		tryAgain = true;
		while (tryAgain) {
			workingSocket = connectionManager.connectToHost(connectionParams, settings);
			if (null == workingSocket) {
				closeApp();
				break;
			}
			logger.info("Connected");

			try {
				workingSocket.setTcpNoDelay(true); // disable Nagle algorithm
				final Reader reader = new Reader(workingSocket.getInputStream());
				final Writer writer = new Writer(workingSocket.getOutputStream());

				workingProtocol = new Protocol(logger, history, reader, writer, new PasswordChooser(passwordFromParams, connectionParams, containerFrame, this), settings);
				workingProtocol.handshake();

				final ClipboardControllerImpl clipboardController = new ClipboardControllerImpl(workingProtocol, settings.getRemoteCharsetName());
				clipboardController.setEnabled(settings.isAllowClipboardTransfer());
				settings.addListener(clipboardController);

				surface = new Surface(logger, vncPointerLocation, workingProtocol, this, uiSettings.getScaleFactor());
				settings.addListener(this);
				uiSettings.addListener(surface);
				containerFrame = createContainer();
				connectionManager.setContainerFrame(containerFrame);
				updateFrameTitle();

				workingProtocol.startNormalHandling(this, surface, clipboardController);
				tryAgain = false;
			} catch (final UnsupportedProtocolVersionException e) {
				connectionManager.showReconnectDialog("Unsupported Protocol Version", e.getMessage());
				logger.debug(e.getMessage());
			} catch (final UnsupportedSecurityTypeException e) {
				connectionManager.showReconnectDialog("Unsupported Security Type", e.getMessage());
				logger.debug(e.getMessage());
			} catch (final AuthenticationFailedException e) {
				passwordFromParams = null;
				connectionManager.showReconnectDialog("Authentication Failed", e.getMessage());
				logger.debug(e.getMessage());
			} catch (final TransportException e) {
				if (!isAppletStopped) {
					connectionManager.showReconnectDialog("Connection Error", "Connection Error" + ": " + e.getMessage());
					logger.debug(e.getMessage());
				}
			} catch (final FatalException e) {
				connectionManager.showReconnectDialog("Connection Error", "Connection Error" + ": " + e.getMessage());
				logger.debug(e.getMessage());
			} catch (final IOException e) {
				connectionManager.showReconnectDialog("Connection Error", "Connection Error" + ": " + e.getMessage());
				logger.debug(e.getMessage());
			}
		}
	}

	private JFrame createContainer() {
		containerManager = new ContainerManager(this);
		final Container container = containerManager.createContainer(surface, isSeparateFrame);

		if (showControls) {
			createButtonsPanel(workingProtocol, containerManager);
			containerManager.registerResizeListener(container);
			containerManager.updateZoomButtonsState();
		}
		setSurfaceToHandleKbdFocus();
		return isSeparateFrame ? (JFrame) container : null;
	}

	public void packContainer() {
		containerManager.pack();
	}

	protected void createButtonsPanel(final ProtocolContext context, final ContainerManager containerManager) {
		final ContainerManager.ButtonsBar buttonsBar = containerManager.createButtonsBar();

		buttonsBar.createButton("options", "Set Options", new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				showOptionsDialog();
				setSurfaceToHandleKbdFocus();
			}
		});

		buttonsBar.createButton("info", "Show connection info", new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				showConnectionInfoMessage(context.getRemoteDesktopName());
				setSurfaceToHandleKbdFocus();
			}
		});

		buttonsBar.createStrut();

		buttonsBar.createButton("refresh", "Refresh screen", new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				context.sendRefreshMessage();
				setSurfaceToHandleKbdFocus();
			}
		});

		containerManager.addZoomButtons();

		kbdButtons = new LinkedList<JComponent>();

		buttonsBar.createStrut();

		final JButton ctrlAltDelButton = buttonsBar.createButton("ctrl-alt-del", "Send 'Ctrl-Alt-Del'", new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				sendCtrlAltDel(context);
				setSurfaceToHandleKbdFocus();
			}
		});
		kbdButtons.add(ctrlAltDelButton);

		final JButton winButton = buttonsBar.createButton("win", "Send 'Win' key as 'Ctrl-Esc'", new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				sendWinKey(context);
				setSurfaceToHandleKbdFocus();
			}
		});
		kbdButtons.add(winButton);

		final JToggleButton ctrlButton = buttonsBar.createToggleButton("ctrl", "Ctrl Lock", new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					context.sendMessage(new KeyEventMessage(logger, Keymap.K_CTRL_LEFT, true));
				} else {
					context.sendMessage(new KeyEventMessage(logger, Keymap.K_CTRL_LEFT, false));
				}
				setSurfaceToHandleKbdFocus();
			}
		});
		kbdButtons.add(ctrlButton);

		final JToggleButton altButton = buttonsBar.createToggleButton("alt", "Alt Lock", new ItemListener() {

			@Override
			public void itemStateChanged(final ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					context.sendMessage(new KeyEventMessage(logger, Keymap.K_ALT_LEFT, true));
				} else {
					context.sendMessage(new KeyEventMessage(logger, Keymap.K_ALT_LEFT, false));
				}
				setSurfaceToHandleKbdFocus();
			}
		});
		kbdButtons.add(altButton);

		final ModifierButtonEventListener modifierButtonListener = new ModifierButtonEventListener();
		modifierButtonListener.addButton(KeyEvent.VK_CONTROL, ctrlButton);
		modifierButtonListener.addButton(KeyEvent.VK_ALT, altButton);
		surface.addModifierListener(modifierButtonListener);

		// JButton fileTransferButton = new JButton(Utils.getButtonIcon("file-transfer"));
		// fileTransferButton.setMargin(buttonsMargin);
		// buttonBar.add(fileTransferButton);

		buttonsBar.createStrut();

		buttonsBar.createButton("close", "Close", new ActionListener() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				closeApp();
			}
		}).setAlignmentX(RIGHT_ALIGNMENT);

		containerManager.setButtonsBarVisible(true);
	}

	void updateFrameTitle() {
		if (containerFrame != null) {
			containerFrame.setTitle(workingProtocol.getRemoteDesktopName() + " [zoom: " + uiSettings.getScalePercentFormatted() + "%]");
		}
	}

	protected void setSurfaceToHandleKbdFocus() {
		if (surface != null && !surface.requestFocusInWindow()) {
			surface.requestFocus();
		}
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

	private void showOptionsDialog() {
		final OptionsDialog optionsDialog = new OptionsDialog(containerFrame);
		optionsDialog.initControlsFromSettings(settings, false);
		optionsDialog.setVisible(true);
	}

	private void showConnectionInfoMessage(final String title) {
		final StringBuilder message = new StringBuilder();
		message.append("Connected to: ").append(title).append("\n");
		message.append("Host: ").append(connectionParams.hostName).append(" Port: ").append(connectionParams.getPortNumber()).append("\n\n");

		message.append("Desktop geometry: ").append(String.valueOf(surface.getWidth())).append(" \u00D7 ") // multiplication
			// sign
			.append(String.valueOf(surface.getHeight())).append("\n");
		message.append("Color format: ").append(String.valueOf(Math.round(Math.pow(2, workingProtocol.getPixelFormat().depth)))).append(" colors (")
			.append(String.valueOf(workingProtocol.getPixelFormat().depth)).append(" bits)\n");
		message.append("Current protocol version: ").append(workingProtocol.getProtocolVersion());
		if (workingProtocol.isTight()) {
			message.append("tight");
		}
		message.append("\n");

		final JOptionPane infoPane = new JOptionPane(message.toString(), JOptionPane.INFORMATION_MESSAGE);
		final JDialog infoDialog = infoPane.createDialog(containerFrame, "VNC connection info");
		infoDialog.setModalityType(ModalityType.MODELESS);
		infoDialog.setVisible(true);
	}

	private void sendCtrlAltDel(final ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_CTRL_LEFT, true));
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_ALT_LEFT, true));
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_DELETE, true));
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_DELETE, false));
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_ALT_LEFT, false));
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_CTRL_LEFT, false));
	}

	private void sendWinKey(final ProtocolContext context) {
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_CTRL_LEFT, true));
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_ESCAPE, true));
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_ESCAPE, false));
		context.sendMessage(new KeyEventMessage(logger, Keymap.K_CTRL_LEFT, false));
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
		closeApp();
	}
}
