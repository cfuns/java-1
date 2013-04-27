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

package com.glavsoft.rfb.protocol;

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
import com.glavsoft.rfb.client.FramebufferUpdateRequestMessage;
import com.glavsoft.rfb.client.SetEncodingsMessage;
import com.glavsoft.rfb.client.SetPixelFormatMessage;
import com.glavsoft.rfb.encoding.PixelFormat;
import com.glavsoft.rfb.encoding.decoder.DecodersContainer;
import com.glavsoft.rfb.protocol.state.HandshakeState;
import com.glavsoft.rfb.protocol.state.ProtocolState;
import com.glavsoft.transport.Reader;
import com.glavsoft.transport.Writer;
import de.benjaminborbe.vnc.core.connector.VncHistory;
import org.slf4j.Logger;

public class Protocol implements ProtocolContext, IChangeSettingsListener {

	private ProtocolState state;

	private final IPasswordRetriever passwordRetriever;

	private final ProtocolSettings settings;

	private int fbWidth;

	private int fbHeight;

	private PixelFormat pixelFormat;

	private final Reader reader;

	private final Writer writer;

	private String remoteDesktopName;

	private MessageQueue messageQueue;

	private final DecodersContainer decoders;

	private SenderTask senderTask;

	private ReceiverTask receiverTask;

	private IRfbSessionListener rfbSessionListener;

	private IRepaintController repaintController;

	private PixelFormat serverPixelFormat;

	private Thread senderThread;

	private Thread receiverThread;

	private boolean isTight;

	private String protocolVersion;

	private final VncHistory history;

	private final Logger logger;

	public Protocol(
		final Logger logger,
		final VncHistory history,
		final Reader reader,
		final Writer writer,
		final IPasswordRetriever passwordRetriever,
		final ProtocolSettings settings
	) {
		this.logger = logger;
		this.history = history;
		this.reader = reader;
		this.writer = writer;
		this.passwordRetriever = passwordRetriever;
		this.settings = settings;
		decoders = new DecodersContainer(logger);
		decoders.instantiateDecodersWhenNeeded(settings.encodings);
		state = new HandshakeState(this);
	}

	@Override
	public void changeStateTo(final ProtocolState state) {
		this.state = state;
	}

	public void handshake() throws UnsupportedProtocolVersionException, UnsupportedSecurityTypeException, AuthenticationFailedException, TransportException, FatalException {
		while (state.next()) {
			// continue;
		}
		this.messageQueue = new MessageQueue(logger);
	}

	@Override
	public PixelFormat getPixelFormat() {
		return pixelFormat;
	}

	@Override
	public void setPixelFormat(final PixelFormat pixelFormat) {
		this.pixelFormat = pixelFormat;
		if (repaintController != null) {
			repaintController.setPixelFormat(pixelFormat);
		}
	}

	@Override
	public String getRemoteDesktopName() {
		return remoteDesktopName;
	}

	@Override
	public void setRemoteDesktopName(final String name) {
		remoteDesktopName = name;
	}

	@Override
	public int getFbWidth() {
		return fbWidth;
	}

	@Override
	public void setFbWidth(final int fbWidth) {
		this.fbWidth = fbWidth;
	}

	@Override
	public int getFbHeight() {
		return fbHeight;
	}

	@Override
	public void setFbHeight(final int fbHeight) {
		this.fbHeight = fbHeight;
	}

	@Override
	public IPasswordRetriever getPasswordRetriever() {
		return passwordRetriever;
	}

	@Override
	public ProtocolSettings getSettings() {
		return settings;
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	@Override
	public Writer getWriter() {
		return writer;
	}

	@Override
	public Reader getReader() {
		return reader;
	}

	/**
	 * Following the server initialisation message it's up to the client to send
	 * whichever protocol messages it wants. Typically it will send a
	 * SetPixelFormat message and a SetEncodings message, followed by a
	 * FramebufferUpdateRequest. From then on the server will send
	 * FramebufferUpdate messages in response to the client's
	 * FramebufferUpdateRequest messages. The client should send
	 * FramebufferUpdateRequest messages with incremental set to true when it has
	 * finished processing one FramebufferUpdate and is ready to process another.
	 * With a fast client, the rate at which FramebufferUpdateRequests are sent
	 * should be regulated to avoid hogging the network.
	 */
	public void startNormalHandling(
		final IRfbSessionListener rfbSessionListener,
		final IRepaintController repaintController,
		final ClipboardController clipboardController
	) {
		logger.debug("startNormalHandling");
		this.rfbSessionListener = rfbSessionListener;
		this.repaintController = repaintController;
		// if (settings.getBitsPerPixel() == 0) {
		// settings.setBitsPerPixel(pixelFormat.bitsPerPixel); // the same the server sent
		// when not initialized yet
		// }
		serverPixelFormat = pixelFormat;
		serverPixelFormat.trueColourFlag = 1; // correct flag - we don't support color maps
		setPixelFormat(createPixelFormat(settings));
		sendMessage(new SetPixelFormatMessage(pixelFormat));
		logger.trace("sent: " + pixelFormat);

		sendSupportedEncodingsMessage(settings);
		settings.addListener(this); // to support pixel format (color depth), and encodings
		// changes
		settings.addListener(repaintController);

		sendRefreshMessage();
		senderTask = new SenderTask(logger, messageQueue, writer, this);
		senderThread = new Thread(senderTask);
		senderThread.start();
		decoders.resetDecoders();
		receiverTask = new ReceiverTask(logger, reader, repaintController, clipboardController, decoders, this);
		receiverThread = new Thread(receiverTask);
		receiverThread.start();
	}

	@Override
	public void sendMessage(final ClientToServerMessage message) {
		history.add(message);
		messageQueue.put(message);
	}

	private void sendSupportedEncodingsMessage(final ProtocolSettings settings) {
		decoders.instantiateDecodersWhenNeeded(settings.encodings);
		final SetEncodingsMessage encodingsMessage = new SetEncodingsMessage(settings.encodings);
		sendMessage(encodingsMessage);
		logger.trace("sent: " + encodingsMessage.toString());
	}

	/**
	 * create pixel format by bpp
	 */
	private PixelFormat createPixelFormat(final ProtocolSettings settings) {
		final int serverBigEndianFlag = serverPixelFormat.bigEndianFlag;
		switch (settings.getBitsPerPixel()) {
			case ProtocolSettings.BPP_32:
				return PixelFormat.create32bppPixelFormat(serverBigEndianFlag);
			case ProtocolSettings.BPP_16:
				return PixelFormat.create16bppPixelFormat(serverBigEndianFlag);
			case ProtocolSettings.BPP_8:
				return PixelFormat.create8bppBGRPixelFormat(serverBigEndianFlag);
			case ProtocolSettings.BPP_6:
				return PixelFormat.create6bppPixelFormat(serverBigEndianFlag);
			case ProtocolSettings.BPP_3:
				return PixelFormat.create3bppPixelFormat(serverBigEndianFlag);
			case ProtocolSettings.BPP_SERVER_SETTINGS:
				return serverPixelFormat;
			default:
				// unsupported bpp, use default
				return PixelFormat.create32bppPixelFormat(serverBigEndianFlag);
		}
	}

	@Override
	public void settingsChanged(final SettingsChangedEvent e) {
		final ProtocolSettings settings = (ProtocolSettings) e.getSource();
		if (settings.isChangedEncodings()) {
			sendSupportedEncodingsMessage(settings);
		}
		if (settings.changedBitsPerPixel() && receiverTask != null) {
			receiverTask.queueUpdatePixelFormat(createPixelFormat(settings));
		}
	}

	@Override
	public void sendRefreshMessage() {
		sendMessage(new FramebufferUpdateRequestMessage(0, 0, fbWidth, fbHeight, false));
		logger.trace("sent: full FB Refresh");
	}

	@Override
	public void cleanUpSession(final String message) {
		cleanUpSession();
		rfbSessionListener.rfbSessionStopped(message);
	}

	public synchronized void cleanUpSession() {
		if (senderTask != null) {
			senderTask.stopTask();
		}
		if (receiverTask != null) {
			receiverTask.stopTask();
		}
		if (senderTask != null) {
			try {
				senderThread.join(1000);
			} catch (final InterruptedException e) {
				// nop
			}
			senderTask = null;
		}
		if (receiverTask != null) {
			try {
				receiverThread.join(1000);
			} catch (final InterruptedException e) {
				// nop
			}
			receiverTask = null;
		}
	}

	@Override
	public void setTight(final boolean isTight) {
		this.isTight = isTight;
	}

	@Override
	public boolean isTight() {
		return isTight;
	}

	@Override
	public void setProtocolVersion(final String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	@Override
	public String getProtocolVersion() {
		return protocolVersion;
	}

	public Renderer getRenderer() {
		return receiverTask.getRenderer();
	}

	public MessageQueue getMessageQueue() {
		return messageQueue;
	}
}
