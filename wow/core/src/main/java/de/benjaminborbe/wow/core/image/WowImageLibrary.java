package de.benjaminborbe.wow.core.image;

import javax.inject.Inject;
import de.benjaminborbe.tools.image.Pixels;
import de.benjaminborbe.tools.image.PixelsImpl;
import de.benjaminborbe.tools.util.ResourceUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WowImageLibrary {

	private final ResourceUtil resourceUtil;

	@Inject
	public WowImageLibrary(final ResourceUtil resourceUtil) {
		this.resourceUtil = resourceUtil;
	}

	private Pixels getFileAsPixels(final String fileName) throws IOException {
		return getFileAsPixels(resourceUtil.getResourceContentAsInputStream(fileName));
	}

	private Collection<Pixels> getFileAsPixels(final String... fileNames) throws IOException {
		final List<Pixels> result = new ArrayList<>();
		for (final String filename : fileNames) {
			result.add(getFileAsPixels(filename));
		}
		return result;
	}

	private Pixels getFileAsPixels(final InputStream file) throws IOException {
		final BufferedImage image = ImageIO.read(file);
		return new PixelsImpl(image);
	}

	public Pixels getFishingButton() throws IOException {
		return getFileAsPixels("wow-fishing-button.bmp");
	}

	public Collection<Pixels> getWowAppIcon() throws IOException {
		return getFileAsPixels("wow-app-icon-v1.bmp", "wow-app-icon-v2.bmp", "wow-app-icon-v3.bmp", "wow-app-icon-v4.bmp", "wow-app-icon-v5.bmp", "wow-app-icon-v6.bmp");
	}

	public Collection<Pixels> getWowPlayButton() throws IOException {
		return getFileAsPixels("wow-play-button-v1.bmp", "wow-play-button-v2.bmp", "wow-play-button-v3.bmp");
	}

	public Collection<Pixels> getWowStartIcon() throws IOException {
		return getFileAsPixels("wow-icon-inner-v1.bmp", "wow-icon-inner-v2.bmp");
	}

	public Collection<Pixels> getWowLoginButton() throws IOException {
		return getFileAsPixels("wow-login-button-v1.bmp", "wow-login-button-v2.bmp");
	}

	public Collection<Pixels> getWowLogoutButton() throws IOException {
		return getFileAsPixels("wow-logout-x.bmp", "wow-logout-quit-button.bmp");
	}

	public Collection<Pixels> getWowEnterWorldButton() throws IOException {
		return getFileAsPixels("wow-enter-world-button-v1.bmp", "wow-enter-world-button-v2.bmp", "wow-enter-world-button-v3.bmp");
	}

}
