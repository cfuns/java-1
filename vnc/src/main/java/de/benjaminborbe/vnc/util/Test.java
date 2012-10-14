package de.benjaminborbe.vnc.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Test {

	public void main(final String[] args) throws IOException {
		final BufferedImage bufferedImage = ImageIO.read(new File("Test"));
		bufferedImage.getRGB(10, 10);

		final int w = bufferedImage.getWidth();
		final int h = bufferedImage.getHeight(null);
		// Get Pixels
		final int[] rgbs = new int[w * h];
		bufferedImage.getRGB(0, 0, w, h, rgbs, 0, w); // Get all pixels
	}
}
