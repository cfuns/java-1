package de.benjaminborbe.gallery.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.gallery.GalleryBean;
import de.benjaminborbe.gallery.gallery.GalleryDao;
import de.benjaminborbe.gallery.image.GalleryImageBean;
import de.benjaminborbe.gallery.image.GalleryImageDao;
import de.benjaminborbe.storage.api.StorageException;

@Singleton
public class GalleryServiceImpl implements GalleryService {

	private final Logger logger;

	private final GalleryImageDao galleryImageDao;

	private final GalleryDao galleryDao;

	@Inject
	public GalleryServiceImpl(final Logger logger, final GalleryDao galleryDao, final GalleryImageDao galleryImageDao) {
		this.logger = logger;
		this.galleryDao = galleryDao;
		this.galleryImageDao = galleryImageDao;
	}

	@Override
	public GalleryImageIdentifier saveImage(final GalleryIdentifier galleryIdentifier, final String name, final String contentType, final byte[] content)
			throws GalleryServiceException {
		try {
			logger.debug("saveImage");
			final GalleryImageBean image = new GalleryImageBean();
			final GalleryImageIdentifier id = createGalleryImageIdentifier(content);
			image.setId(id);
			image.setGalleryIdentifier(galleryIdentifier);
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
	public List<GalleryImageIdentifier> getImageIdentifiers(final GalleryIdentifier galleryIdentifier) throws GalleryServiceException {
		try {
			logger.debug("getImages - GallerIdentifier: " + galleryIdentifier);
			final List<GalleryImageIdentifier> result = new ArrayList<GalleryImageIdentifier>(galleryImageDao.getGalleryImageIdentifiers(galleryIdentifier));
			logger.debug("getImages - GallerIdentifier: " + galleryIdentifier + " => " + result.size());
			return result;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void deleteImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		try {
			logger.debug("deleteImage - GalleryImageIdentifier " + id);
			galleryImageDao.delete(id);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	protected GalleryImageIdentifier createGalleryImageIdentifier(final byte[] content) throws GalleryServiceException {
		logger.debug("createGalleryImageIdentifier");
		return createGalleryImageIdentifier(String.valueOf(UUID.nameUUIDFromBytes(content)));
	}

	@Override
	public GalleryImageIdentifier createGalleryImageIdentifier(final String id) throws GalleryServiceException {
		logger.debug("createGalleryImageIdentifier");
		return new GalleryImageIdentifier(id);
	}

	@Override
	public GalleryImage getImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		try {
			logger.debug("getImage");
			final GalleryImageBean image = galleryImageDao.load(id);
			if (image != null) {
				logger.debug("getImageContent name: " + image.getName() + " length: " + image.getContent().length);
			}
			else {
				logger.debug("no image found for id: " + id);
			}
			return image;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryIdentifier createGallery(final String name) throws GalleryServiceException {
		try {
			logger.debug("createGallery name: " + name);
			final String id = String.valueOf(UUID.nameUUIDFromBytes(name.getBytes()));
			final GalleryIdentifier galleryIdentifier = createGalleryIdentifier(id);
			final GalleryBean gallery = galleryDao.create();
			gallery.setId(galleryIdentifier);
			gallery.setName(name);
			galleryDao.save(gallery);
			return galleryIdentifier;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryIdentifier createGalleryIdentifier(final String id) {
		logger.debug("createGalleryIdentifier");
		return new GalleryIdentifier(id);
	}

	@Override
	public void deleteGallery(final GalleryIdentifier galleryIdentifier) throws GalleryServiceException {
		try {
			logger.debug("deleteGallery");
			// delete all images of gallery
			final List<GalleryImageIdentifier> images = getImageIdentifiers(galleryIdentifier);
			for (final GalleryImageIdentifier image : images) {
				galleryImageDao.delete(image);
			}
			// delete gallery
			galleryDao.delete(galleryIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<GalleryIdentifier> getGalleryIdentifiers() throws GalleryServiceException {
		try {
			logger.debug("getGalleryIdentifiers");
			return galleryDao.getIdentifiers();
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Gallery getGallery(final GalleryIdentifier galleryIdentifier) throws GalleryServiceException {
		try {
			logger.debug("getGallery");
			return galleryDao.load(galleryIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<Gallery> getGalleries() throws GalleryServiceException {
		try {
			logger.debug("getGalleries");
			return new ArrayList<Gallery>(galleryDao.getAll());
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}
}
