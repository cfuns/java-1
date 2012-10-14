package de.benjaminborbe.vnc.util;

import com.glavsoft.drawing.Renderer;
import com.glavsoft.viewer.Viewer;
import com.google.inject.Inject;

import de.benjaminborbe.vnc.api.VncScreenContent;

public class VncScreenContentImpl implements VncScreenContent {

	private final Viewer viewer;

	@Inject
	public VncScreenContentImpl(final Viewer viewer) {
		this.viewer = viewer;
	}

	private Renderer getRenderer() {
		return viewer.getRenderer();
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
		final int[] pixels = getRenderer().getPixels();
		return pixels[getWidth() * (y - 1) + x - 1];
	}

	/**
	 * Start and end position are included
	 */
	@Override
	public int[][] getPixels(final int xstart, final int ystart, final int xend, final int yend) {
		final int[][] result = new int[xend - xstart + 1][yend - ystart + 1];
		for (int x = xstart; x <= xend; ++x) {
			for (final int y = ystart; y <= yend; ++x) {
				result[x - xstart][y - ystart] = getPixel(x, y);
			}
		}
		return result;
	}

}
