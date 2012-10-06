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
	public List<GalleryImageIdentifier> getImages(final GalleryIdentifier galleryIdentifier) throws GalleryServiceException {
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
			logger.debug("getImageContent name: " + image.getName() + " length: " + image.getContent().length);
			return image;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryIdentifier createGallery(final String name) throws GalleryServiceException {
		try {
			logger.debug("createGallery");
			final GalleryIdentifier id = createGalleryIdentifier(name);
			final GalleryBean gallery = galleryDao.create();
			gallery.setId(id);
			gallery.setName(name);
			galleryDao.save(gallery);
			return id;
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
			galleryDao.delete(galleryIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<GalleryIdentifier> getGalleries() throws GalleryServiceException {
		try {
			logger.debug("getGalleries");
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
}
