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

import com.glavsoft.drawing.Renderer;
import com.glavsoft.rfb.encoding.PixelFormat;
import com.glavsoft.rfb.encoding.decoder.FramebufferUpdateRectangle;
import com.glavsoft.transport.Reader;
import org.slf4j.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RendererImpl extends Renderer implements ImageObserver {

	private final BufferedImage offscreanImage;

	public RendererImpl(final Logger logger, final Reader reader, int width, int height, final PixelFormat pixelFormat) {
		super(logger);
		logger.debug("new RendererImpl");
		if (0 == width)
			width = 1;
		if (0 == height)
			height = 1;
		init(reader, width, height, pixelFormat);
		final ColorModel colorModel = new DirectColorModel(24, 0xff0000, 0xff00, 0xff);
		final SampleModel sampleModel = colorModel.createCompatibleSampleModel(width, height);

		final DataBuffer dataBuffer = new DataBufferInt(pixels, width * height);
		final WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, null);

		offscreanImage = new BufferedImage(colorModel, raster, false, null);

		cursor = new SoftCursorImpl(0, 0, 0, 0);
	}

	/**
	 * Draw jpeg image data
	 *
	 * @param bytes
	 * jpeg image data array
	 * @param offset
	 * start offset at data array
	 * @param jpegBufferLength
	 * jpeg image data array length
	 * @param rect
	 * image location and dimensions
	 */
	private final CyclicBarrier barier = new CyclicBarrier(2);

	@Override
	public void drawJpegImage(final byte[] bytes, final int offset, final int jpegBufferLength, final FramebufferUpdateRectangle rect) {
		final Image jpegImage = Toolkit.getDefaultToolkit().createImage(bytes, offset, jpegBufferLength);
		Toolkit.getDefaultToolkit().prepareImage(jpegImage, -1, -1, this);
		try {
			barier.await(3, TimeUnit.SECONDS);
		} catch (final InterruptedException e) {
			// nop
		} catch (BrokenBarrierException e) {
			// nop
		} catch (TimeoutException e) {
			// nop
		}
		final Graphics graphics = offscreanImage.getGraphics();
		graphics.drawImage(jpegImage, rect.x, rect.y, rect.width, rect.height, this);
	}

	@Override
	public boolean imageUpdate(final Image img, final int infoflags, final int x, final int y, final int width, final int height) {
		final boolean isReady = (infoflags & (ALLBITS | ABORT)) != 0;
		if (isReady) {
			try {
				barier.await();
			} catch (final BrokenBarrierException e) {
				// nop
			} catch (InterruptedException e) {
				// nop
			}
		}
		return !isReady;
	}

	/* Swing specific interface */
	public Image getOffscreenImage() {
		return offscreanImage;
	}

	@Override
	public BufferedImage getBufferedImage() {
		return offscreanImage;
	}

	public SoftCursorImpl getCursor() {
		return (SoftCursorImpl) cursor;
	}

}
