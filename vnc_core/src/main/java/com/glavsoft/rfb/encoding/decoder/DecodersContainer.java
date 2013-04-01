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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

import com.glavsoft.rfb.encoding.EncodingType;

/**
 * Decoders container class
 */
public class DecodersContainer {

	private static final Map<EncodingType, Class<? extends Decoder>> knownDecoders = new HashMap<EncodingType, Class<? extends Decoder>>();
	static {
		knownDecoders.put(EncodingType.TIGHT, TightDecoder.class);
		knownDecoders.put(EncodingType.HEXTILE, HextileDecoder.class);
		knownDecoders.put(EncodingType.ZRLE, ZRLEDecoder.class);
		knownDecoders.put(EncodingType.ZLIB, ZlibDecoder.class);
		knownDecoders.put(EncodingType.RRE, RREDecoder.class);
		knownDecoders.put(EncodingType.COPY_RECT, CopyRectDecoder.class);
		// knownDecoders.put(EncodingType.RAW_ENCODING, RawDecoder.class);
	}

	private final Map<EncodingType, Decoder> decoders = new HashMap<EncodingType, Decoder>();

	private final Logger logger;

	public DecodersContainer(final Logger logger) {
		this.logger = logger;
		decoders.put(EncodingType.RAW_ENCODING, RawDecoder.getInstance());
	}

	/**
	 * Instantiate decoders for encodings we are going to use.
	 * 
	 * @param encodings
	 *          encodings we need to handle
	 */
	public void instantiateDecodersWhenNeeded(final Collection<EncodingType> encodings) {
		for (final EncodingType enc : encodings) {
			if (EncodingType.ordinaryEncodings.contains(enc) && !decoders.containsKey(enc)) {
				try {
					decoders.put(enc, knownDecoders.get(enc).newInstance());
				}
				catch (final InstantiationException e) {
					logError(enc, e);
				}
				catch (final IllegalAccessException e) {
					logError(enc, e);
				}
			}
		}
	}

	private void logError(final EncodingType enc, final Exception e) {
		logger.debug("Can not instantiate decoder for encoding type '" + enc.getName() + "' " + e.getMessage());
	}

	public Decoder getDecoderByType(final EncodingType type) {
		return decoders.get(type);
	}

	public void resetDecoders() {
		for (final Decoder decoder : decoders.values()) {
			if (decoder != null) {
				decoder.reset();
			}
		}
	}

}
