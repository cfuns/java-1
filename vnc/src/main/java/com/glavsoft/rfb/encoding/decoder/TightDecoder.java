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

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import com.glavsoft.drawing.ColorDecoder;
import com.glavsoft.drawing.Renderer;
import com.glavsoft.exceptions.TransportException;
import com.glavsoft.transport.Reader;

/**
 * Tight protocol extention decoder
 */
public class TightDecoder extends Decoder {

	private static final int FILL_TYPE = 0x08;

	private static final int JPEG_TYPE = 0x09;

	private static final int FILTER_ID_MASK = 0x40;

	private static final int STREAM_ID_MASK = 0x30;

	private static final int BASIC_FILTER = 0x00;

	private static final int PALETTE_FILTER = 0x01;

	private static final int GRADIENT_FILTER = 0x02;

	private static final int MIN_SIZE_TO_COMPRESS = 12;

	static final int DECODERS_NUM = 4;

	Inflater[] decoders;

	private int decoderId;

	final static int tightZlibBufferSize = 512;

	public TightDecoder() {
		reset();
	}

	@Override
	public void decode(final Reader reader, final Renderer renderer, final FramebufferUpdateRectangle rect) throws TransportException {
		final int bytesPerPixel = renderer.getBytesPerPixelSignificant();

		/**
		 * bits
		 * 7 - FILL or JPEG type
		 * 6 - filter presence flag
		 * 5, 4 - decoder to use when Basic type (bit 7 not set)
		 * or
		 * 4 - JPEG type when set bit 7
		 * 3 - reset decoder #3
		 * 2 - reset decoder #2
		 * 1 - reset decoder #1
		 * 0 - reset decoder #0
		 */
		final int compControl = reader.readUInt8();
		resetDecoders(compControl);

		final int compType = compControl >> 4 & 0x0F;
		switch (compType) {
		case FILL_TYPE:
			final int color = renderer.readTightPixelColor(reader);
			renderer.fillRect(color, rect);
			break;
		case JPEG_TYPE:
			if (bytesPerPixel != 3) {
				// throw new EncodingException(
				// "Tight doesn't support JPEG subencoding while depth not equal to 24bpp is used");
			}
			processJpegType(reader, renderer, rect);
			break;
		default:
			if (compType > JPEG_TYPE) {
				// throw new EncodingException(
				// "Compression control byte is incorrect!");
			}
			else {
				processBasicType(compControl, reader, renderer, rect);
			}
		}
	}

	private void processBasicType(final int compControl, final Reader reader, final Renderer renderer, final FramebufferUpdateRectangle rect) throws TransportException {
		decoderId = (compControl & STREAM_ID_MASK) >> 4;

		int filterId = 0;
		if ((compControl & FILTER_ID_MASK) > 0) { // filter byte presence
			filterId = reader.readUInt8();
		}
		final int bytesPerCPixel = renderer.getBytesPerPixelSignificant();
		final int lengthCurrentbpp = bytesPerCPixel * rect.width * rect.height;
		byte[] buffer;
		switch (filterId) {
		case BASIC_FILTER:
			buffer = readTightData(lengthCurrentbpp, reader);
			renderer.drawTightBytes(buffer, 0, rect.x, rect.y, rect.width, rect.height);
			break;
		case PALETTE_FILTER:
			final int paletteSize = reader.readUInt8() + 1;
			final int[] palette = readPalette(paletteSize, reader, renderer);
			final int dataLength = paletteSize == 2 ? rect.height * ((rect.width + 7) / 8) : rect.width * rect.height;
			buffer = readTightData(dataLength, reader);
			renderer.drawBytesWithPalette(buffer, rect, palette);
			break;
		case GRADIENT_FILTER:
			/*
			 * The "gradient" filter pre-processes pixel data with a simple algorithm
			 * which converts each color component to a difference between a "predicted"
			 * intensity and the actual intensity. Such a technique does not affect
			 * uncompressed data size, but helps to compress photo-like images better.
			 * Pseudo-code for converting intensities to differences is the following:
			 * P[i,j] := V[i-1,j] + V[i,j-1] - V[i-1,j-1];
			 * if (P[i,j] < 0) then P[i,j] := 0;
			 * if (P[i,j] > MAX) then P[i,j] := MAX;
			 * D[i,j] := V[i,j] - P[i,j];
			 * Here V[i,j] is the intensity of a color component for a pixel at
			 * coordinates (i,j). MAX is the maximum value of intensity for a color
			 * component.
			 */
			buffer = readTightData(bytesPerCPixel * rect.width * rect.height, reader);
			final byte[][] opRows = new byte[2][rect.width * 3 + 3];
			int opRowIndex = 0;
			final byte[] components = new byte[3];
			int pixelOffset = 0;
			final ColorDecoder colorDecoder = renderer.getColorDecoder();
			for (int i = 0; i < rect.height; ++i) {
				// exchange thisRow and prevRow:
				final byte[] thisRow = opRows[opRowIndex];
				final byte[] prevRow = opRows[opRowIndex = (opRowIndex + 1) % 2];
				for (int j = 3; j < rect.width * 3 + 3; j += 3) {
					colorDecoder.fillRawComponents(components, buffer, pixelOffset);
					pixelOffset += bytesPerCPixel;
					int d = (0xff & prevRow[j + 0]) + // "upper" pixel (from prev row)
							(0xff & thisRow[j + 0 - 3]) - // prev pixel
							(0xff & prevRow[j + 0 - 3]); // "diagonal" prev pixel
					thisRow[j + 0] = (byte) (components[0] + (d < 0 ? 0 : d > colorDecoder.redMax ? colorDecoder.redMax : d) & colorDecoder.redMax);
					d = (0xff & prevRow[j + 1]) + (0xff & thisRow[j + 1 - 3]) - (0xff & prevRow[j + 1 - 3]);
					thisRow[j + 1] = (byte) (components[1] + (d < 0 ? 0 : d > colorDecoder.greenMax ? colorDecoder.greenMax : d) & colorDecoder.greenMax);
					d = (0xff & prevRow[j + 2]) + (0xff & thisRow[j + 2 - 3]) - (0xff & prevRow[j + 2 - 3]);
					thisRow[j + 2] = (byte) (components[2] + (d < 0 ? 0 : d > colorDecoder.blueMax ? colorDecoder.blueMax : d) & colorDecoder.blueMax);
				}
				renderer.drawUncaliberedRGBLine(thisRow, rect.x, rect.y + i, rect.width);
			}

			break;
		default:
			break;
		}
	}

	/**
	 * Read palette from reader
	 */
	private int[] readPalette(final int paletteSize, final Reader reader, final Renderer renderer) throws TransportException {
		/**
		 * When bytesPerPixel == 1 && paletteSize == 2 read 2 bytes of palette
		 * When bytesPerPixel == 1 && paletteSize != 2 - error
		 * When bytesPerPixel == 3 (4) read (paletteSize * 3) bytes of palette
		 * so use renderer.readPixelColor
		 */
		final int[] palette = new int[paletteSize];
		for (int i = 0; i < palette.length; ++i) {
			palette[i] = renderer.readTightPixelColor(reader);
		}
		return palette;
	}

	/**
	 * Reads compressed (expected length >= MIN_SIZE_TO_COMPRESS) or
	 * uncompressed data. When compressed decompresses it.
	 * 
	 * @param expectedLength
	 *          expected data length in bytes
	 * @param reader
	 * @return result data
	 * @throws TransportException
	 */
	private byte[] readTightData(final int expectedLength, final Reader reader) throws TransportException {
		if (expectedLength < MIN_SIZE_TO_COMPRESS) {
			final byte[] buffer = ByteBuffer.getInstance().getBuffer(expectedLength);
			reader.readBytes(buffer, 0, expectedLength);
			return buffer;
		}
		else
			return readCompressedData(expectedLength, reader);
	}

	/**
	 * Reads compressed data length, then read compressed data into rawBuffer
	 * and decompress data with expected length == length
	 * 
	 * Note: returned data contains not only decompressed data but raw data at array tail
	 * which need to be ignored. Use only first expectedLength bytes.
	 * 
	 * @param expectedLength
	 *          expected data length
	 * @param reader
	 * @return decompressed data (length == expectedLength) / + followed raw data (ignore,
	 *         please)
	 * @throws TransportException
	 */
	private byte[] readCompressedData(final int expectedLength, final Reader reader) throws TransportException {
		final int rawDataLength = readCompactSize(reader);

		final byte[] buffer = ByteBuffer.getInstance().getBuffer(expectedLength + rawDataLength);
		// read compressed (raw) data behind space allocated for decompressed data
		reader.readBytes(buffer, expectedLength, rawDataLength);
		if (null == decoders[decoderId]) {
			decoders[decoderId] = new Inflater();
		}
		final Inflater decoder = decoders[decoderId];
		decoder.setInput(buffer, expectedLength, rawDataLength);
		try {
			decoder.inflate(buffer, 0, expectedLength);
		}
		catch (final DataFormatException e) {
			// logger.debug("TightDecoder", "readCompressedData", e);
			throw new TransportException("cannot inflate tight compressed data", e);
		}
		return buffer;
	}

	private void processJpegType(final Reader reader, final Renderer renderer, final FramebufferUpdateRectangle rect) throws TransportException {
		final int jpegBufferLength = readCompactSize(reader);
		final byte[] bytes = ByteBuffer.getInstance().getBuffer(jpegBufferLength);
		reader.readBytes(bytes, 0, jpegBufferLength);
		renderer.drawJpegImage(bytes, 0, jpegBufferLength, rect);
	}

	/**
	 * Read an integer from reader in compact representation (from 1 to 3 bytes).
	 * Highest bit of read byte set to 1 means next byte contains data.
	 * Lower 7 bit of each byte contains significant data. Max bytes = 3.
	 * Less significant bytes first order.
	 * 
	 * @param reader
	 * @return int value
	 * @throws TransportException
	 */
	private int readCompactSize(final Reader reader) throws TransportException {
		int b = reader.readUInt8();
		int size = b & 0x7F;
		if ((b & 0x80) != 0) {
			b = reader.readUInt8();
			size += (b & 0x7F) << 7;
			if ((b & 0x80) != 0) {
				size += reader.readUInt8() << 14;
			}
		}
		return size;
	}

	/**
	 * Flush (reset) zlib decoders when bits 3, 2, 1, 0 of compControl is set
	 * 
	 * @param compControl
	 */
	private void resetDecoders(int compControl) {
		for (int i = 0; i < DECODERS_NUM; ++i) {
			if ((compControl & 1) != 0 && decoders[i] != null) {
				decoders[i].reset();
			}
			compControl >>= 1;
		}

	}

	@Override
	public void reset() {
		decoders = new Inflater[DECODERS_NUM];
	}

}