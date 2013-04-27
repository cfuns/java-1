package de.benjaminborbe.vnc.core.util;

import com.glavsoft.drawing.Renderer;
import de.benjaminborbe.vnc.api.VncLocation;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.api.VncScreenContent;
import de.benjaminborbe.vnc.core.connector.VncConnector;
import de.benjaminborbe.vnc.core.connector.VncPointerLocation;

import javax.inject.Inject;

public class VncScreenContentImpl implements VncScreenContent {

	private final VncPointerLocation vncPointerLocation;

	private final VncConnector vncConnector;

	@Inject
	public VncScreenContentImpl(final VncConnector vncConnector, final VncPointerLocation vncPointerLocation) {
		this.vncConnector = vncConnector;
		this.vncPointerLocation = vncPointerLocation;
	}

	private Renderer getRenderer() {
		return vncConnector.getViewer().getRenderer();
	}

	@Override
	public int getWidth() {
		return getRenderer().getWidth();
	}

	@Override
	public int getHeight() {
		return getRenderer().getHeight();
	}

	/**
	 * x: 1 <-> width
	 * y: 1 <-> height
	 */
	@Override
	public int getPixel(final int x, final int y) {
		return getPixels().getPixel(x, y);
	}

	/**
	 * Start and end position are included
	 */
	@Override
	public VncPixels getPixels(final int xstart, final int ystart, final int xend, final int yend) {
		return getPixels().getSubPixel(xstart, ystart, xend, yend);
	}

	@Override
	public VncPixels getPixels() {
		return new VncPixelsImpl(getRenderer().getBufferedImage());
	}

	// public int[][] getPixelArray(final int xstart, final int ystart, final int xend,
	// final int yend) {
	// final int[][] result = new int[xend - xstart + 1][yend - ystart + 1];
	// for (int x = xstart; x <= xend; ++x) {
	// for (final int y = ystart; y <= yend; ++x) {
	// result[x - xstart][y - ystart] = getPixel(x, y);
	// }
	// }
	// return result;
	// }

	@Override
	public VncLocation getPointerLocation() {
		return vncPointerLocation;
	}

}
