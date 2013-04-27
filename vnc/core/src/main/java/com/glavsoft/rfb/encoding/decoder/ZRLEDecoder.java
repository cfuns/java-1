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

package com.glavsoft.rfb.encoding.decoder;

import com.glavsoft.drawing.Renderer;
import com.glavsoft.exceptions.TransportException;
import com.glavsoft.transport.Reader;

public class ZRLEDecoder extends ZlibDecoder {

	private static final int DEFAULT_TILE_SIZE = 64;

	@Override
	public void decode(final Reader reader, final Renderer renderer, final FramebufferUpdateRectangle rect) throws TransportException {
		final int zippedLength = (int) reader.readUInt32();
		if (0 == zippedLength)
			return;
		final int length = rect.width * rect.height * renderer.getBytesPerPixel();
		final byte[] bytes = unzip(reader, zippedLength, length);
		int offset = zippedLength;
		final int maxX = rect.x + rect.width;
		final int maxY = rect.y + rect.height;
		final int[] palette = new int[128];
		for (int tileY = rect.y; tileY < maxY; tileY += DEFAULT_TILE_SIZE) {
			final int tileHeight = Math.min(maxY - tileY, DEFAULT_TILE_SIZE);

			for (int tileX = rect.x; tileX < maxX; tileX += DEFAULT_TILE_SIZE) {
				final int tileWidth = Math.min(maxX - tileX, DEFAULT_TILE_SIZE);
				final int subencoding = bytes[offset++] & 0x0ff;
				// 128 -plain RLE, 130-255 - Palette RLE
				final boolean isRle = (subencoding & 128) != 0;
				// 2 to 16 for raw packed palette data, 130 to 255 for Palette RLE (subencoding -
				// 128)
				final int paletteSize = subencoding & 127;
				offset += readPalette(bytes, offset, renderer, palette, paletteSize);
				if (1 == subencoding) { // A solid tile consisting of a single colour
					renderer.fillRect(palette[0], tileX, tileY, tileWidth, tileHeight);
					continue;
				}
				if (isRle) {
					if (0 == paletteSize) { // subencoding == 128 (or paletteSize == 0) - Plain RLE
						offset += decodePlainRle(bytes, offset, renderer, tileX, tileY, tileWidth, tileHeight);
					} else {
						offset += decodePaletteRle(bytes, offset, renderer, palette, tileX, tileY, tileWidth, tileHeight, paletteSize);
					}
				} else {
					if (0 == paletteSize) { // subencoding == 0 (or paletteSize == 0) - raw CPIXEL
						// data
						offset += decodeRaw(bytes, offset, renderer, tileX, tileY, tileWidth, tileHeight);
					} else {
						offset += decodePacked(bytes, offset, renderer, palette, paletteSize, tileX, tileY, tileWidth, tileHeight);
					}
				}
			}
		}

	}

	private int decodePlainRle(
		final byte[] bytes,
		final int offset,
		final Renderer renderer,
		final int tileX,
		final int tileY,
		final int tileWidth,
		final int tileHeight
	) {
		final int bytesPerCPixel = renderer.getBytesPerPixelSignificant();
		final int[] decodedBitmap = new int[tileWidth * tileHeight];
		int decodedOffset = 0;
		final int decodedEnd = tileWidth * tileHeight;
		int index = offset;
		while (decodedOffset < decodedEnd) {
			final int color = renderer.getCompactPixelColor(bytes, index);
			index += bytesPerCPixel;
			int rlength = 1;
			do {
				rlength += bytes[index] & 0x0ff;
			} while ((bytes[index++] & 0x0ff) == 255);
			assert rlength <= decodedEnd - decodedOffset;
			renderer.fillColorBitmapWithColor(decodedBitmap, decodedOffset, rlength, color);
			decodedOffset += rlength;
		}
		renderer.drawColoredBitmap(decodedBitmap, tileX, tileY, tileWidth, tileHeight);
		return index - offset;
	}

	private int decodePaletteRle(
		final byte[] bytes, final int offset, final Renderer renderer, final int[] palette, final int tileX, final int tileY, final int tileWidth,
		final int tileHeight, final int paletteSize
	) {
		final int[] decodedBitmap = new int[tileWidth * tileHeight];
		int decodedOffset = 0;
		final int decodedEnd = tileWidth * tileHeight;
		int index = offset;
		while (decodedOffset < decodedEnd) {
			final int colorIndex = bytes[index++];
			final int color = palette[colorIndex & 127];
			int rlength = 1;
			if ((colorIndex & 128) != 0) {
				do {
					rlength += bytes[index] & 0x0ff;
				} while (bytes[index++] == (byte) 255);
			}
			assert rlength <= decodedEnd - decodedOffset;
			renderer.fillColorBitmapWithColor(decodedBitmap, decodedOffset, rlength, color);
			decodedOffset += rlength;
		}
		renderer.drawColoredBitmap(decodedBitmap, tileX, tileY, tileWidth, tileHeight);
		return index - offset;
	}

	private int decodePacked(
		final byte[] bytes, final int offset, final Renderer renderer, final int[] palette, final int paletteSize, final int tileX, final int tileY,
		final int tileWidth, final int tileHeight
	) {
		final int[] decodedBytes = new int[tileWidth * tileHeight];
		final int bitsPerPalletedPixel = paletteSize > 16 ? 8 : paletteSize > 4 ? 4 : paletteSize > 2 ? 2 : 1;
		int packedOffset = offset;
		int decodedOffset = 0;
		for (int i = 0; i < tileHeight; ++i) {
			final int decodedRowEnd = decodedOffset + tileWidth;
			int byteProcessed = 0;
			int bitsRemain = 0;

			while (decodedOffset < decodedRowEnd) {
				if (bitsRemain == 0) {
					byteProcessed = bytes[packedOffset++];
					bitsRemain = 8;
				}
				bitsRemain -= bitsPerPalletedPixel;
				final int index = byteProcessed >> bitsRemain & (1 << bitsPerPalletedPixel) - 1 & 127;
				final int color = palette[index];
				renderer.fillColorBitmapWithColor(decodedBytes, decodedOffset, 1, color);
				++decodedOffset;
			}
		}
		renderer.drawColoredBitmap(decodedBytes, tileX, tileY, tileWidth, tileHeight);
		return packedOffset - offset;
	}

	private int decodeRaw(
		final byte[] bytes,
		final int offset,
		final Renderer renderer,
		final int tileX,
		final int tileY,
		final int tileWidth,
		final int tileHeight
	)
		throws TransportException {
		return renderer.drawCompactBytes(bytes, offset, tileX, tileY, tileWidth, tileHeight);
	}

	private int readPalette(final byte[] bytes, final int offset, final Renderer renderer, final int[] palette, final int paletteSize) {
		for (int i = 0; i < paletteSize; ++i) {
			palette[i] = renderer.getCompactPixelColor(bytes, offset + i * renderer.getBytesPerPixelSignificant());
		}
		return paletteSize * renderer.getBytesPerPixelSignificant();
	}

}
