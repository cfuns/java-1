package de.benjaminborbe.websearch.core.action;

import de.benjaminborbe.httpdownloader.api.HttpContent;
import de.benjaminborbe.httpdownloader.api.HttpHeader;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.Md5Util;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

public class WebsearchSaveAllImages {

	public static final int LONG_MIN = 1000;

	public static final int SHORT_MIN = 600;

	private final Logger logger;

	private final WebsearchPageDao websearchPageDao;

	private final Md5Util md5Util;

	@Inject
	public WebsearchSaveAllImages(final Logger logger, final WebsearchPageDao websearchPageDao, final Md5Util md5Util) {
		this.logger = logger;
		this.websearchPageDao = websearchPageDao;
		this.md5Util = md5Util;
	}

	public void saveImages() throws EntityIteratorException, StorageException {
		logger.trace("saveImages started");
		final EntityIterator<WebsearchPageBean> i = websearchPageDao.getEntityIterator();
		while (i.hasNext()) {
			final WebsearchPageBean websearchPageBean = i.next();
			try {
				logger.trace("handle page " + websearchPageBean.getUrl());
				saveImage(websearchPageBean);
			} catch (NoSuchAlgorithmException | IOException e) {
				logger.warn("save image " + websearchPageBean.getId() + " failed", e);
			}
		}
		logger.trace("saveImages finished");
	}

	private void saveImage(final WebsearchPageBean websearchPageBean) throws NoSuchAlgorithmException, IOException {
		final boolean jpeg = isJpeg(websearchPageBean);
		final boolean correctSize = isCorrectSize(websearchPageBean);
		logger.trace("isJpeg: " + jpeg + " correctSize: " + correctSize);
		if (jpeg && correctSize) {
			logger.debug("save image of page " + websearchPageBean.getUrl());

			final byte[] content = websearchPageBean.getContent().getContent();
			final File filename = buildFile(content);
			saveJpeg(filename, content);
		}
	}

	private boolean isCorrectSize(final WebsearchPageBean websearchPageBean) {
		try {
			final HttpContent httpContent = websearchPageBean.getContent();
			if (httpContent == null) {
				return false;
			}
			final InputStream contentStream = httpContent.getContentStream();
			if (contentStream == null) {
				return false;
			}
			final BufferedImage image = ImageIO.read(contentStream);
			return image != null && (image.getWidth() >= LONG_MIN && image.getHeight() >= SHORT_MIN || image.getWidth() >= SHORT_MIN && image.getHeight() >= LONG_MIN);
		} catch (Exception e) {
			return false;
		}
	}

	private void saveJpeg(final File file, final byte[] content) throws NoSuchAlgorithmException, IOException {
		final FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(content);
		outputStream.close();
	}

	private File buildFile(final byte[] content) throws NoSuchAlgorithmException {
		return new File("/tmp/" + new String(Hex.encodeHex(md5Util.getMd5(content))) + ".jpg");
	}

	private boolean isJpeg(final WebsearchPageBean websearchPageBean) {
		final HttpHeader header = websearchPageBean.getHeader();
		return header != null && "image/jpeg".equals(header.getValue("Content-Type"));
	}
}
