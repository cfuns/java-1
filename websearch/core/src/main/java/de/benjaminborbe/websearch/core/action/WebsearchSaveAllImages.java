package de.benjaminborbe.websearch.core.action;

import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.websearch.core.dao.WebsearchPageBean;
import de.benjaminborbe.websearch.core.dao.WebsearchPageDao;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class WebsearchSaveAllImages {

	private final Logger logger;

	private final WebsearchPageDao websearchPageDao;

	private final WebsearchSaveAllImage websearchSaveAllImage;

	@Inject
	public WebsearchSaveAllImages(
		final Logger logger,
		final WebsearchPageDao websearchPageDao,
		final WebsearchSaveAllImage websearchSaveAllImage
	) {
		this.logger = logger;
		this.websearchPageDao = websearchPageDao;
		this.websearchSaveAllImage = websearchSaveAllImage;
	}

	public void saveImages() throws EntityIteratorException, StorageException {
		logger.trace("saveImages started");
		final EntityIterator<WebsearchPageBean> i = websearchPageDao.getEntityIterator();
		while (i.hasNext()) {
			final WebsearchPageBean websearchPageBean = i.next();
			try {
				logger.trace("handle page " + websearchPageBean.getUrl());
				websearchSaveAllImage.saveImage(websearchPageBean);
			} catch (NoSuchAlgorithmException | IOException e) {
				logger.warn("save image " + websearchPageBean.getId() + " failed", e);
			}
		}
		logger.trace("saveImages finished");
	}

}
