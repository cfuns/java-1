package de.benjaminborbe.vnc.util;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;

import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncLocationImpl;
import de.benjaminborbe.vnc.api.VncPixels;

public class VncPixelsDiffer {

	@Inject
	public VncPixelsDiffer() {
	}

	public List<VncLocation> diff(final VncPixels pixelsA, final VncPixels pixelsB, final int mask) {
		return diff(new VncPixelsColorFilter(pixelsA, mask), new VncPixelsColorFilter(pixelsB, mask));
	}

	public List<VncLocation> diff(final VncPixels pixelsA, final VncPixels pixelsB) {
		final List<VncLocation> result = new ArrayList<VncLocation>();
		if (pixelsA.getWidth() != pixelsB.getWidth() || pixelsA.getHeight() != pixelsB.getHeight()) {
			throw new IllegalArgumentException("different size");
		}
		for (int x = 1; x <= pixelsA.getWidth(); x++) {
			for (int y = 1; y <= pixelsA.getHeight(); y++) {
				if (pixelsA.getPixel(x, y) != pixelsB.getPixel(x, y)) {
					result.add(new VncLocationImpl(x, y));
				}
			}
		}
		return result;
	}
}
