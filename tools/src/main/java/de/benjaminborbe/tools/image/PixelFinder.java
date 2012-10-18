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
		final List<Coordinate> result = new ArrayList<Coordinate>();
		for (int xi = 1; xi <= image.getWidth() - subImage.getWidth() + 1; ++xi) {
			for (int yi = 1; yi <= image.getHeight() - subImage.getHeight() + 1; ++yi) {
				if (isMatch(image, subImage, xi, yi)) {
					result.add(new Coordinate(xi, yi));
				}
			}
		}
		logger.debug("found " + result.size() + " matches");
		return result;
	}

	private boolean isMatch(final Pixels image, final Pixels subImage, final int xi, final int yi) {
		for (int xs = 1; xs <= subImage.getWidth(); ++xs) {
			for (int ys = 1; ys <= subImage.getHeight(); ++ys) {
				if (image.getPixel(xi + xs - 1, yi + ys - 1) != subImage.getPixel(xs, ys)) {
					return false;
				}
			}
		}
		return true;
	}
}
