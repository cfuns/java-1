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

import com.glavsoft.rfb.client.ClientToServerMessage;
import org.slf4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author dime at tightvnc.com
 */
public class MessageQueue {

	private final BlockingQueue<ClientToServerMessage> queue;

	private final Logger logger;

	public MessageQueue(final Logger logger) {
		this.logger = logger;
		queue = new LinkedBlockingQueue<>();
	}

	public void put(final ClientToServerMessage message) {
		logger.trace("put message to queue");
		queue.offer(message);
	}

	public ClientToServerMessage get() throws InterruptedException {
		logger.trace("remove message to queue");
		return queue.take();
	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}
}
