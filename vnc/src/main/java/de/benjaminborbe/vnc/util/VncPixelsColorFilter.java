package de.benjaminborbe.vnc.util;

import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsColorFilter extends VncPixelsAdapter implements VncPixels {

	public VncPixelsColorFilter(final VncPixels vncPixels, final int filter) {
		super(vncPixels);
	}

}
