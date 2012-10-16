package de.benjaminborbe.vnc.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public void storeImage() throws VncConnectorException, IOException {
		final VncPixels vncPixels = vncConnector.getScreenContent().getPixels();
		storeImage(vncPixels);
	}

	public void storeImage(final VncPixels vncPixels) throws IOException {
		storeImage(vncPixels, "vnc-" + calendarUtil.toDateTimeString(calendarUtil.now()));
	}

	public void storeImage(final VncPixels vncPixels, final String name) throws IOException {
		final File file = new File("/tmp/" + name + ".bmp");
		storeImage(vncPixels, file);
	}

	public void storeImage(final VncPixels vncPixels, final File file) throws IOException {
		logger.debug("storePixelToFile");
		final OutputStream outputStream = new FileOutputStream(file);
		// final RenderedImage image =
		// vncConnector.getViewer().getRenderer().getBufferedImage();
		bmpUtil.writeBMP(outputStream, buildBufferedImage(vncPixels, vncPixels.getWidth(), vncPixels.getHeight()), 96);
		outputStream.flush();
		outputStream.close();
	}

	private BufferedImage buildBufferedImage(final VncPixels vncPixels, final int width, final int height) {
		final BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; ++x) {
			for (int y = 0; y < height; ++y) {
				bi.setRGB(x, y, vncPixels.getPixel(x + 1, y + 1));
			}
		}
		return bi;
	}

}
