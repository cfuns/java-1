package de.benjaminborbe.gallery.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.image.GalleryImageBean;
import de.benjaminborbe.gallery.image.GalleryImageDao;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public class GalleryServiceImpl implements GalleryService {

	private final Logger logger;

	private final GalleryImageDao galleryImageDao;

	// private final Map<GalleryImageIdentifier, GalleryImageBean> images = new
	// HashMap<GalleryImageIdentifier, GalleryImageBean>();

	@Inject
	public GalleryServiceImpl(final Logger logger, final GalleryImageDao galleryImageDao) {
		this.logger = logger;
		this.galleryImageDao = galleryImageDao;
	}

	@Override
	public GalleryImageIdentifier saveImage(final String name, final String contentType, final byte[] content) throws GalleryServiceException {
		try {
			logger.debug("saveImage");
			final GalleryImageBean image = new GalleryImageBean();
			final GalleryImageIdentifier id = createGalleryImageIdentifier(content);
			image.setId(id);
			image.setName(name);
			image.setContentType(contentType);
			image.setContent(content);
			galleryImageDao.save(image);
			logger.debug("saveImage name: " + name + " length: " + content.length);
			return id;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public List<GalleryImageIdentifier> getImages() throws GalleryServiceException {
		try {
			logger.debug("getImageNames");
			return new ArrayList<GalleryImageIdentifier>(galleryImageDao.getIdentifiers());
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void deleteImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		try {
			galleryImageDao.delete(id);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	protected GalleryImageIdentifier createGalleryImageIdentifier(final byte[] content) throws GalleryServiceException {
		return createGalleryImageIdentifier(String.valueOf(UUID.nameUUIDFromBytes(content)));
	}

	@Override
	public GalleryImageIdentifier createGalleryImageIdentifier(final String id) throws GalleryServiceException {
		return new GalleryImageIdentifier(id);
	}

	@Override
	public GalleryImage getImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		try {
			logger.debug("getImage");
			final GalleryImageBean image = galleryImageDao.load(id);
			logger.debug("getImageContent name: " + image.getName() + " length: " + image.getContent().length);
			return image;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}
}
