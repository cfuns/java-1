package de.benjaminborbe.vnc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.inject.Inject;

import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.StreamUtil;
import de.benjaminborbe.vnc.api.VncPixels;
import de.benjaminborbe.vnc.connector.VncConnector;
import de.benjaminborbe.vnc.connector.VncConnectorException;

public class VncStoreImageContentAction {

	private final VncConnector vncConnector;

	private final CalendarUtil calendarUtil;

	private final StreamUtil streamUtil;

	@Inject
	public VncStoreImageContentAction(final VncConnector vncConnector, final CalendarUtil calendarUtil, final StreamUtil streamUtil) {
		this.vncConnector = vncConnector;
		this.calendarUtil = calendarUtil;
		this.streamUtil = streamUtil;
	}

	public void storeImageContent() throws VncConnectorException, IOException {
		final VncPixels vncPixels = vncConnector.getScreenContent().getPixels();
		final File file = new File("/tmp/vnc-" + calendarUtil.toDateTimeString(calendarUtil.now()) + ".dump");
		storePixelToFile(vncPixels, file);
	}

	public void storePixelToFile(final VncPixels vncPixels, final File file) throws IOException {
		final InputStream inputStream = vncPixels.getInputStream();
		final OutputStream outputStream = new FileOutputStream(file);
		streamUtil.copy(inputStream, outputStream);
		inputStream.close();
		outputStream.flush();
		outputStream.close();
	}
}
