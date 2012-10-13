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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import com.glavsoft.core.SettingsChangedEvent;
import com.glavsoft.drawing.Renderer;
import com.glavsoft.rfb.IChangeSettingsListener;
import com.glavsoft.rfb.IRepaintController;
import com.glavsoft.rfb.encoding.PixelFormat;
import com.glavsoft.rfb.encoding.decoder.FramebufferUpdateRectangle;
import com.glavsoft.rfb.protocol.ProtocolContext;
import com.glavsoft.rfb.protocol.ProtocolSettings;
import com.glavsoft.transport.Reader;
import com.glavsoft.viewer.Viewer;

@SuppressWarnings("serial")
public class Surface extends JPanel implements IRepaintController, IChangeSettingsListener {

	private int width;

	private int height;

	private SoftCursorImpl cursor;

	private RendererImpl renderer;

	private MouseEventListener mouseEventListener;

	private KeyEventListener keyEventListener;

	private boolean showCursor;

	private ModifierButtonEventListener modifierButtonListener;

	private boolean isUserInputEnabled = false;

	private final ProtocolContext context;

	private double scaleFactor;

	private final Viewer viewer;

	public Dimension oldSize;

	@Override
	public boolean isDoubleBuffered() {
		return false;
	}

	public Surface(final ProtocolContext context, final Viewer viewer, final double scaleFactor) {
		this.context = context;
		this.viewer = viewer;
		this.scaleFactor = scaleFactor;
		init(context.getFbWidth(), context.getFbHeight());
		oldSize = getPreferredSize();

		if (!context.getSettings().isViewOnly()) {
			setUserInputEnabled(true, context.getSettings().isConvertToAscii());
		}
		showCursor = context.getSettings().isShowRemoteCursor();
	}

	private void setUserInputEnabled(final boolean enable, final boolean convertToAscii) {
		if (enable == isUserInputEnabled)
			return;
		isUserInputEnabled = enable;
		if (enable) {
			if (null == mouseEventListener) {
				mouseEventListener = new MouseEventListener(this, context, scaleFactor);
			}
			addMouseListener(mouseEventListener);
			addMouseMotionListener(mouseEventListener);
			addMouseWheelListener(mouseEventListener);

			setFocusTraversalKeysEnabled(false);
			if (null == keyEventListener) {
				keyEventListener = new KeyEventListener(context);
				if (modifierButtonListener != null) {
					keyEventListener.addModifierListener(modifierButtonListener);
				}
			}
			keyEventListener.setConvertToAscii(convertToAscii);
			addKeyListener(keyEventListener);
			enableInputMethods(false);
		}
		else {
			removeMouseListener(mouseEventListener);
			removeMouseMotionListener(mouseEventListener);
			removeMouseWheelListener(mouseEventListener);
			removeKeyListener(keyEventListener);
		}
	}

	@Override
	public Renderer createRenderer(final Reader reader, final int width, final int height, final PixelFormat pixelFormat) {
		renderer = new RendererImpl(reader, width, height, pixelFormat);
		synchronized (renderer) {
			cursor = renderer.getCursor();
		}
		init(renderer.getWidth(), renderer.getHeight());
		updateFrameSize();
		return renderer;
	}

	private void init(final int width, final int height) {
		this.width = width;
		this.height = height;
		setSize(getPreferredSize());
	}

	private void updateFrameSize() {
		setSize(getPreferredSize());
		viewer.packContainer();
		requestFocus();
	}

	@Override
	public void paintComponent(final Graphics g) {
		((Graphics2D) g).scale(scaleFactor, scaleFactor);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		synchronized (renderer) {
			final Image offscreenImage = renderer.getOffscreenImage();
			if (offscreenImage != null) {
				g.drawImage(offscreenImage, 0, 0, null);
			}
		}
		synchronized (cursor) {
			final Image cursorImage = cursor.getImage();
			if (showCursor && cursorImage != null && (scaleFactor != 1 || g.getClipBounds().intersects(cursor.rX, cursor.rY, cursor.width, cursor.height))) {
				g.drawImage(cursorImage, cursor.rX, cursor.rY, null);
			}
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension((int) (this.width * scaleFactor), (int) (this.height * scaleFactor));
	}

	@Override
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

	@Override
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}

	/**
	 * Saves context and simply invokes native JPanel repaint method which
	 * asyncroniously register repaint request using invokeLater to repaint be
	 * runned in Swing event dispatcher thread. So may be called from other
	 * threads.
	 */
	@Override
	public void repaintBitmap(final FramebufferUpdateRectangle rect) {
		repaintBitmap(rect.x, rect.y, rect.width, rect.height);
	}

	@Override
	public void repaintBitmap(final int x, final int y, final int width, final int height) {
		repaint((int) (x * scaleFactor), (int) (y * scaleFactor), (int) Math.ceil(width * scaleFactor), (int) Math.ceil(height * scaleFactor));
	}

	@Override
	public void repaintCursor() {
		synchronized (cursor) {
			repaint((int) (cursor.oldRX * scaleFactor), (int) (cursor.oldRY * scaleFactor), (int) Math.ceil(cursor.oldWidth * scaleFactor) + 1,
					(int) Math.ceil(cursor.oldHeight * scaleFactor) + 1);
			repaint((int) (cursor.rX * scaleFactor), (int) (cursor.rY * scaleFactor), (int) Math.ceil(cursor.width * scaleFactor) + 1, (int) Math.ceil(cursor.height * scaleFactor) + 1);
		}
	}

	@Override
	public void updateCursorPosition(final short x, final short y) {
		synchronized (cursor) {
			cursor.updatePosition(x, y);
			repaintCursor();
		}
	}

	private void showCursor(final boolean show) {
		synchronized (cursor) {
			showCursor = show;
		}
	}

	public void addModifierListener(final ModifierButtonEventListener modifierButtonListener) {
		this.modifierButtonListener = modifierButtonListener;
		if (keyEventListener != null) {
			keyEventListener.addModifierListener(modifierButtonListener);
		}
	}

	@Override
	public void settingsChanged(final SettingsChangedEvent e) {
		if (ProtocolSettings.isRfbSettingsChangedFired(e)) {
			final ProtocolSettings settings = (ProtocolSettings) e.getSource();
			setUserInputEnabled(!settings.isViewOnly(), settings.isConvertToAscii());
			showCursor(settings.isShowRemoteCursor());
		}
		else if (UiSettings.isUiSettingsChangedFired(e)) {
			final UiSettings settings = (UiSettings) e.getSource();
			oldSize = getPreferredSize();
			scaleFactor = settings.getScaleFactor();
		}
		mouseEventListener.setScaleFactor(scaleFactor);
		updateFrameSize();
	}

	@Override
	public void setPixelFormat(final PixelFormat pixelFormat) {
		if (renderer != null) {
			renderer.initPixelFormat(pixelFormat);
		}
	}

}
