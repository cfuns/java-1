package de.benjaminborbe.wow.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.google.inject.Inject;

import de.benjaminborbe.tools.image.Pixels;
import de.benjaminborbe.tools.image.PixelsImpl;
import de.benjaminborbe.tools.util.ResourceUtil;

public class WowImageLibrary {

	private final ResourceUtil resourceUtil;

	@Inject
	public WowImageLibrary(final ResourceUtil resourceUtil) {
		this.resourceUtil = resourceUtil;
	}

	private Pixels getFileAsPixels(final String fileName) throws IOException {
		return getFileAsPixels(resourceUtil.getResourceContentAsInputStream(fileName));
	}

	private Pixels getFileAsPixels(final InputStream file) throws IOException {
		final BufferedImage image = ImageIO.read(file);
		return new PixelsImpl(image);
	}

	public Pixels getFishingButton() throws IOException {
		return getFileAsPixels("wow-fishing-button.bmp");
	}

	public Pixels getWowAppIcon() throws IOException {
		return getFileAsPixels("wow-app-icon.bmp");
	}
}
