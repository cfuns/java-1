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

	public Pixels getWowAppIconV1() throws IOException {
		return getFileAsPixels("wow-app-icon-v1.bmp");
	}

	public Pixels getWowAppIconV2() throws IOException {
		return getFileAsPixels("wow-app-icon-v2.bmp");
	}

	public Pixels getWowAppIconV3() throws IOException {
		return getFileAsPixels("wow-app-icon-v3.bmp");
	}

	public Pixels getWowAppIconV4() throws IOException {
		return getFileAsPixels("wow-app-icon-v4.bmp");
	}

	public Pixels getWowAppIconV5() throws IOException {
		return getFileAsPixels("wow-app-icon-v5.bmp");
	}

	public Pixels getWowAppIconV6() throws IOException {
		return getFileAsPixels("wow-app-icon-v6.bmp");
	}
}
