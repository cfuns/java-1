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

import com.glavsoft.exceptions.TransportException;
import com.glavsoft.rfb.client.ClientToServerMessage;
import com.glavsoft.transport.Writer;
import org.slf4j.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

public class SenderTask implements Runnable {

	private final MessageQueue queue;

	private final Writer writer;

	private final ProtocolContext protocolContext;

	private volatile boolean isRunning = false;

	private final Logger logger;

	/**
	 * Create sender task
	 * Task runs as thread, receive messages from queue and sends them to writer.
	 * When no messages appears in queue longer than timeout period, sends FramebufferUpdate
	 * request
	 *
	 * @param messageQueue    queue to poll messages
	 * @param writer          writer to send messages out
	 * @param protocolContext
	 */
	public SenderTask(final Logger logger, final MessageQueue messageQueue, final Writer writer, final ProtocolContext protocolContext) {
		this.logger = logger;
		this.queue = messageQueue;
		this.writer = writer;
		this.protocolContext = protocolContext;
	}

	@Override
	public void run() {
		isRunning = true;
		while (isRunning) {
			final ClientToServerMessage message;
			try {
				message = queue.get();
				if (message != null) {
					message.send(writer);
				}
			} catch (final InterruptedException e) {
				// nop
			} catch (final TransportException e) {
				logger.debug("Close session: " + e.getMessage());
				if (isRunning) {
					protocolContext.cleanUpSession("Connection closed");
				}
				stopTask();
			} catch (final Throwable te) {
				final StringWriter sw = new StringWriter();
				final PrintWriter pw = new PrintWriter(sw);
				te.printStackTrace(pw);
				if (isRunning) {
					protocolContext.cleanUpSession(te.getMessage() + "\n" + sw.toString());
				}
				stopTask();
			}
		}
	}

	public void stopTask() {
		isRunning = false;
	}

}
