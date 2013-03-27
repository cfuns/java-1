package de.benjaminborbe.tools.image;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

public class PixelsDiffer {

	private final Logger logger;

	@Inject
	public PixelsDiffer(final Logger logger) {
		this.logger = logger;
	}

	public List<Coordinate> diff(final Pixels pixelsA, final Pixels pixelsB) {
		logger.debug("diff");

		return diff(pixelsA, pixelsB, new PixelFilterNop());
	}

	public List<Coordinate> diff(final Pixels pixelsA, final Pixels pixelsB, final PixelFilter pixelFilter) {
		logger.debug("diff");

		final List<Coordinate> result = new ArrayList<Coordinate>();
		if (pixelsA.getWidth() != pixelsB.getWidth() || pixelsA.getHeight() != pixelsB.getHeight()) {
			throw new IllegalArgumentException("different size");
		}
		for (int x = 1; x <= pixelsA.getWidth(); x++) {
			for (int y = 1; y <= pixelsA.getHeight(); y++) {
				final int pixelA = pixelFilter.filter(pixelsA.getPixel(x, y));
				final int pixelB = pixelFilter.filter(pixelsB.getPixel(x, y));
				if (pixelA != pixelB) {
					result.add(new Coordinate(x, y));
				}
			}
		}

		return result;
	}

	// public List<Coordinate> diff(final Pixels pixelsAM, final Pixels pixelsBM, final int
	// mask, final int t) {
	// logger.debug("diff");
	//
	// final Pixels pixelsA = new PixelsColorFilter(pixelsAM, mask);
	// final Pixels pixelsB = new PixelsColorFilter(pixelsBM, mask);
	//
	//
	//
	//
	// final Pixel a = new Pixel(pixelsA.getPixel(x, y));
	// final Pixel b = new Pixel(pixelsB.getPixel(x, y));
	//
	// if (a.isBlue() && b.isRed() && 64 <= b.getRed() && b.getRed() <= 68 && 580 < x && x <
	// 1300 && 500 < y && y < 759) {
	// result.add(new Coordinate(x, y));
	// }
	// // if (x > 900 && x < 950 && y < 465 && y > 440) {
	// // System.out.println(Math.abs(a.getGrey() - b.getGreen()));
	// // }
	// if (Math.abs(a.getGrey() - b.getGreen()) < 50 && Math.abs(a.getGrey() -
	// b.getGreen()) > 30) {

	// }

	// result.add(new CoordinateImpl(x, y));
	// }
	// if (Math.abs(a.getRed() - b.getRed()) > t || Math.abs(a.getGreen() -
	// b.getGreen()) > t || Math.abs(a.getBlue() - b.getBlue()) > t) {
	// result.add(new CoordinateImpl(x, y));
	// }
	// System.err.println("red-diff: " + Math.abs(a.getRed() - b.getRed()));
	// System.err.println("green-diff: " + Math.abs(a.getGreen() - b.getGreen()));
	// System.err.println("blue-diff: " + Math.abs(a.getBlue() - b.getBlue()));

	// System.err.println("-------");
	// System.err.println(Integer.toHexString(a.getPixel()));
	// System.err.println(Integer.toHexString(b.getPixel()));
	// }
	// if (a.getPixel() < 0x40000 && b.getPixel() > 0x4400000) {

	// System.err.println("compare (" + x + "," + y + ") " + Integer.toHexString(a)
	// +
	// "==" + Integer.toHexString(b));

	//
	// if (x == 1006 && y == 409) {
	// System.err.println(Integer.toHexString(pixelsAM.getPixel(x, y)));
	// System.err.println(Integer.toHexString(pixelsBM.getPixel(x, y)));
	// System.err.println(Integer.toHexString(pixelsA.getPixel(x, y)));
	// System.err.println(Integer.toHexString(pixelsB.getPixel(x, y)));
	// System.err.println("red  =" + Math.abs(a.getRed() - b.getRed()));
	// System.err.println("green=" + Math.abs(a.getGreen() - b.getGreen()));
	// System.err.println("blue =" + Math.abs(a.getBlue() - b.getBlue()));
	// }
	// // }
	// }
	// }
	// return result;
	// }
}
