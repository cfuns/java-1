package de.benjaminborbe.tools.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;

import org.junit.Test;

public class BMPUtilIntegrationTest {

	@Test
	public void testCreateBmp() throws Exception {
		final BMPUtil bmpUtil = new BMPUtil();
		final File file = new File("/tmp/test.bmp");
		final FileOutputStream out = new FileOutputStream(file);
		final int imageWidth = 200;
		final int imageHeight = 100;
		final BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		final int startx = 0;
		final int starty = 0;
		final int w = imageWidth;
		final int h = imageHeight;
		final int[] rgbArray = new int[imageWidth * imageHeight];
		fill(rgbArray);
		final int offset = 0;
		final int scansize = 0;
		image.setRGB(startx, starty, w, h, rgbArray, offset, scansize);
		bmpUtil.writeBMP(out, image, 300);
	}

	private void fill(final int[] rgbArray) {
		for (int i = 0; i < rgbArray.length; ++i) {
			rgbArray[i] = 0xAAAAAA; // grey
			rgbArray[i] = 0x000000; // black
			rgbArray[i] = 0xFFFFFF; // grey
		}
	}
}
