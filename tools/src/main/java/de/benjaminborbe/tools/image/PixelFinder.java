package de.benjaminborbe.tools.image;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;

import com.google.inject.Inject;

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
		for (int xs = 1; xs <= subImage.getWidth(); ++xs) {
			for (int ys = 1; ys <= subImage.getHeight(); ++ys) {
				final int pb = subImage.getPixel(xs, ys);
				final int pa = image.getPixel(xi + xs - 1, yi + ys - 1);
				if (pa == pb) {
					// nop
				}
				else if (100 == matchInPercent) {
					return false;
				}
				else {
					final Pixel pixela = new Pixel(pa);
					final Pixel pixelb = new Pixel(pb);
					if (match(pixela.getRed(), pixelb.getRed(), matchInPercent)) {
						return false;
					}
					if (match(pixela.getBlue(), pixelb.getBlue(), matchInPercent)) {
						return false;
					}
					if (match(pixela.getGreen(), pixelb.getGreen(), matchInPercent)) {
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
