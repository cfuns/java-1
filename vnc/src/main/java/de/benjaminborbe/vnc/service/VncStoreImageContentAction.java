package de.benjaminborbe.vnc.service;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;

import com.google.inject.Inject;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.image.BMPUtil;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.connector.VncConnector;
import de.benjaminborbe.vnc.connector.VncConnectorException;

public class VncStoreImageContentAction {

	private final VncConnector vncConnector;

	private final CalendarUtil calendarUtil;

	private final BMPUtil bmpUtil;

	private final Logger logger;

	@Inject
	public VncStoreImageContentAction(final Logger logger, final VncConnector vncConnector, final CalendarUtil calendarUtil, final BMPUtil bmpUtil) {
		this.logger = logger;
		this.vncConnector = vncConnector;
		this.calendarUtil = calendarUtil;
		this.bmpUtil = bmpUtil;
	}

	public void storeImageContent() throws VncConnectorException, IOException {
		final VncPixels vncPixels = vncConnector.getScreenContent().getPixels();
		final File file = new File("/tmp/vnc-" + calendarUtil.toDateTimeString(calendarUtil.now()) + ".bmp");
		storePixelToFile(vncPixels, file);
	}

	public void storePixelToFile(final VncPixels vncPixels, final File file) throws IOException {
		logger.debug("storePixelToFile");
		final InputStream inputStream = vncPixels.getInputStream();
		final int width = vncPixels.getWidth();
		final int height = vncPixels.getHeight();
		logger.debug("storePixelToFile - width: " + width + " height: " + height);
		final int[] pixels = vncPixels.getPixels();

		if (pixels.length != width * height) {
			logger.warn("invalid pixels length!");
		}

		// final int[] rgbArray = new int[width * height];
		// int r = -1;
		// int counter = 0;
		// while ((r = inputStream.read()) != -1) {
		// rgbArray[counter] = r;
		// // logger.debug(Integer.toHexString(r));
		// counter++;
		// }
		final OutputStream outputStream = new FileOutputStream(file);

		final RenderedImage image = vncConnector.getViewer().getRenderer().getBufferedImage();
		// bmpUtil.writeBMP(outputStream, width, height, pixels, 96);
		bmpUtil.writeBMP(outputStream, image, 96);

		outputStream.flush();
		outputStream.close();
		inputStream.close();
	}
}
