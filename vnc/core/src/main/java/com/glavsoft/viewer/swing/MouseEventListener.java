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

package com.glavsoft.viewer.swing;

import com.glavsoft.rfb.IRepaintController;
import com.glavsoft.rfb.client.PointerEventMessage;
import com.glavsoft.rfb.protocol.ProtocolContext;
import de.benjaminborbe.vnc.core.connector.VncPointerLocation;
import org.slf4j.Logger;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseEventListener extends MouseInputAdapter implements MouseWheelListener {

	private static final byte BUTTON_LEFT = 1;

	private static final byte BUTTON_MIDDLE = 1 << 1;

	private static final byte BUTTON_RIGHT = 1 << 2;

	private static final byte WHEEL_UP = 1 << 3;

	private static final byte WHEEL_DOWN = 1 << 4;

	private final IRepaintController repaintController;

	private final ProtocolContext context;

	private volatile double scaleFactor;

	private final VncPointerLocation vncPointerLocation;

	private final Logger logger;

	public MouseEventListener(
		final Logger logger,
		final VncPointerLocation vncPointerLocation,
		final IRepaintController repaintController,
		final ProtocolContext context,
		final double scaleFactor) {
		this.logger = logger;
		this.vncPointerLocation = vncPointerLocation;
		this.repaintController = repaintController;
		this.context = context;
		this.scaleFactor = scaleFactor;
	}

	public void processMouseEvent(MouseEvent mouseEvent, final MouseWheelEvent mouseWheelEvent, final boolean moved) {

		byte buttonMask = 0;
		if (null == mouseEvent && mouseWheelEvent != null) {
			mouseEvent = mouseWheelEvent;
		}
		assert mouseEvent != null;
		final short x = (short) (mouseEvent.getX() / scaleFactor);
		final short y = (short) (mouseEvent.getY() / scaleFactor);
		if (moved) {
			repaintController.updateCursorPosition(x, y);
		}

		final int modifiersEx = mouseEvent.getModifiersEx();
		// left
		buttonMask |= (modifiersEx & InputEvent.BUTTON1_DOWN_MASK) != 0 ? BUTTON_LEFT : 0;
		// middle
		buttonMask |= (modifiersEx & InputEvent.BUTTON2_DOWN_MASK) != 0 ? BUTTON_MIDDLE : 0;
		// right
		buttonMask |= (modifiersEx & InputEvent.BUTTON3_DOWN_MASK) != 0 ? BUTTON_RIGHT : 0;

		// wheel
		if (mouseWheelEvent != null) {
			int notches = mouseWheelEvent.getWheelRotation();
			final byte wheelMask = notches < 0 ? WHEEL_UP : WHEEL_DOWN;
			// handle more then 1 notches
			notches = Math.abs(notches);
			for (int i = 1; i < notches; ++i) {
				context.sendMessage(new PointerEventMessage(logger, vncPointerLocation, (byte) (buttonMask | wheelMask), x, y));
				context.sendMessage(new PointerEventMessage(logger, vncPointerLocation, buttonMask, x, y));
			}
			context.sendMessage(new PointerEventMessage(logger, vncPointerLocation, (byte) (buttonMask | wheelMask), x, y));
		}
		context.sendMessage(new PointerEventMessage(logger, vncPointerLocation, buttonMask, x, y));
	}

	@Override
	public void mousePressed(final MouseEvent mouseEvent) {
		processMouseEvent(mouseEvent, null, false);
	}

	@Override
	public void mouseReleased(final MouseEvent mouseEvent) {
		processMouseEvent(mouseEvent, null, false);
	}

	@Override
	public void mouseEntered(final MouseEvent mouseEvent) {
	}

	@Override
	public void mouseDragged(final MouseEvent mouseEvent) {
		processMouseEvent(mouseEvent, null, true);
	}

	@Override
	public void mouseMoved(final MouseEvent mouseEvent) {
		processMouseEvent(mouseEvent, null, true);
	}

	@Override
	public void mouseWheelMoved(final MouseWheelEvent emouseWheelEvent) {
		processMouseEvent(null, emouseWheelEvent, false);
	}

	public void setScaleFactor(final double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}
}
