package de.benjaminborbe.tools.image;

import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PixelFinder {

	private final Logger logger;

	@Inject
	public PixelFinder(final Logger logger) {
		this.logger = logger;
	}

	public Collection<Coordinate> find(final Pixels image, final Pixels subImage) {
		return find(image, subImage, 100);
	}

	public Collection<Coordinate> find(final Pixels image, final Pixels subImage, final int matchInPercent) {
		final List<Coordinate> result = new ArrayList<Coordinate>();
		for (int xi = 1; xi <= image.getWidth() - subImage.getWidth() + 1; ++xi) {
			for (int yi = 1; yi <= image.getHeight() - subImage.getHeight() + 1; ++yi) {
				if (isMatch(image, subImage, xi, yi, matchInPercent)) {
					result.add(new Coordinate(xi, yi));
				}
			}
		}
		logger.debug("found " + result.size() + " matches");
		return result;
	}

	private boolean isMatch(final Pixels image, final Pixels subImage, final int xi, final int yi, final int matchInPercent) {
		// int counter = 0;
		for (int xs = 1; xs <= subImage.getWidth(); ++xs) {
			for (int ys = 1; ys <= subImage.getHeight(); ++ys) {
				final int pb = subImage.getPixel(xs, ys);
				final int pa = image.getPixel(xi + xs - 1, yi + ys - 1);
				if (pa == pb) {
					// counter++;
				} else if (100 == matchInPercent) {
					return false;
				} else {
					final Pixel pixela = new Pixel(pa);
					final Pixel pixelb = new Pixel(pb);
					if (match(pixela.getRed(), pixelb.getRed(), matchInPercent) && match(pixela.getBlue(), pixelb.getBlue(), matchInPercent)
						&& match(pixela.getGreen(), pixelb.getGreen(), matchInPercent)) {
						// counter++;
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected boolean match(final int colorA, final int colorB, final int matchInPercent) {
		return getMatchInPercent(colorA, colorB) >= matchInPercent;
	}

	protected int getMatchInPercent(final int colorA, final int colorB) {
		final int diff = Math.abs(colorA - colorB);
		return 100 - 100 * diff / 255;
	}
}
