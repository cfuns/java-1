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

package com.glavsoft.rfb.client;

import org.slf4j.Logger;

import com.glavsoft.exceptions.TransportException;
import com.glavsoft.transport.Writer;

import de.benjaminborbe.vnc.connector.VncPointerLocation;

public class PointerEventMessage implements ClientToServerMessage {

	private final byte buttonMask;

	private final short x;

	private final short y;

	private final VncPointerLocation vncPointerLocation;

	private final Logger logger;

	public PointerEventMessage(final Logger logger, final VncPointerLocation vncPointerLocation, final byte buttonMask, final short x, final short y) {
		this.logger = logger;
		this.vncPointerLocation = vncPointerLocation;
		this.buttonMask = buttonMask;
		this.x = x;
		this.y = y;
	}

	@Override
	public void send(final Writer writer) throws TransportException {
		writer.writeByte(POINTER_EVENT);
		writer.writeByte(buttonMask);
		writer.writeInt16(x);
		writer.writeInt16(y);
		writer.flush();
		vncPointerLocation.set(x, y);
		logger.trace("pointer x:" + x + " y:" + y + " mask:" + buttonMask);
	}

	@Override
	public String toString() {
		return "PointerEventMessage: [x: " + x + ", y: " + y + ", button-mask: " + buttonMask + "]";
	}

}
