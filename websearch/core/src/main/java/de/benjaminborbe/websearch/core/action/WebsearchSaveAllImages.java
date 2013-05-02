package de.benjaminborbe.websearch.core.action;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.tools.util.Base64Util;
import de.benjaminborbe.tools.util.Md5Util;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class WebsearchSaveAllImages {

	private final Logger logger;

	private final WebsearchPageDao websearchPageDao;

	private final Md5Util md5Util;

	private final Base64Util base64Util;

	@Inject
	public WebsearchSaveAllImages(Logger logger, final WebsearchPageDao websearchPageDao, Md5Util md5Util, Base64Util base64Util) {
		this.logger = logger;
		this.websearchPageDao = websearchPageDao;
		this.md5Util = md5Util;
		this.base64Util = base64Util;
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
		if (isJpeg(websearchPageBean)) {
			logger.debug("save image of page " + websearchPageBean.getUrl());
			byte[] content = websearchPageBean.getContent().getContent();
			File filename = buildFile(content);
			saveJpeg(filename, content);
		}
	}

	private void saveJpeg(File file, final byte[] content) throws NoSuchAlgorithmException, IOException {
		final FileOutputStream outputStream = new FileOutputStream(file);
		outputStream.write(content);
		outputStream.close();
	}

	private File buildFile(byte[] content) throws NoSuchAlgorithmException {
		return new File("/tmp/" + base64Util.encode(md5Util.getMd5(content)) + ".jpg");
	}

	private boolean isJpeg(final WebsearchPageBean websearchPageBean) {
		return "image/jpeg".equals(websearchPageBean.getHeader().getValue("Content-Type"));
	}
}
