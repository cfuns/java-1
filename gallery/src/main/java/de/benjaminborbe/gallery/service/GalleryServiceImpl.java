package de.benjaminborbe.gallery.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.dao.GalleryCollectionBean;
import de.benjaminborbe.gallery.dao.GalleryCollectionDao;
import de.benjaminborbe.gallery.dao.GalleryEntryBean;
import de.benjaminborbe.gallery.dao.GalleryEntryDao;
import de.benjaminborbe.gallery.dao.GalleryImageDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.util.IdGeneratorUUID;

@Singleton
public class GalleryServiceImpl implements GalleryService {

	private final Logger logger;

	private final GalleryEntryDao galleryEntryDao;

	private final GalleryCollectionDao galleryCollectionDao;

	private final GalleryImageDao galleryImageDao;

	private final IdGeneratorUUID idGeneratorUUID;

	@Inject
	public GalleryServiceImpl(
			final Logger logger,
			final GalleryCollectionDao galleryDao,
			final GalleryEntryDao galleryEntryDao,
			final IdGeneratorUUID idGeneratorUUID,
			final GalleryImageDao galleryImageDao) {
		this.logger = logger;
		this.galleryCollectionDao = galleryDao;
		this.galleryEntryDao = galleryEntryDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.galleryImageDao = galleryImageDao;
	}

	@Override
	public List<GalleryEntryIdentifier> getEntryIdentifiers(final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
		try {
			logger.debug("getImages - GallerIdentifier: " + galleryIdentifier);
			final List<GalleryEntryIdentifier> result = new ArrayList<GalleryEntryIdentifier>(galleryEntryDao.getGalleryImageIdentifiers(galleryIdentifier));
			logger.debug("getImages - GallerIdentifier: " + galleryIdentifier + " => " + result.size());
			return result;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void deleteEntry(final GalleryEntryIdentifier id) throws GalleryServiceException {
		try {
			logger.debug("deleteImage - GalleryImageIdentifier " + id);
			galleryEntryDao.delete(id);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryEntryIdentifier createEntryIdentifier(final String id) throws GalleryServiceException {
		logger.debug("createGalleryImageIdentifier");
		return new GalleryEntryIdentifier(id);
	}

	@Override
	public GalleryEntry getEntry(final GalleryEntryIdentifier id) throws GalleryServiceException {
		try {
			logger.debug("getImage");
			final GalleryEntryBean image = galleryEntryDao.load(id);
			return image;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryCollectionIdentifier createCollection(final String name) throws GalleryServiceException {
		try {
			logger.debug("createGallery name: " + name);
			final GalleryCollectionIdentifier galleryIdentifier = createCollectionIdentifier(idGeneratorUUID.nextId());
			final GalleryCollectionBean gallery = galleryCollectionDao.create();
			gallery.setId(galleryIdentifier);
			gallery.setName(name);
			galleryCollectionDao.save(gallery);
			return galleryIdentifier;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryCollectionIdentifier createCollectionIdentifier(final String id) {
		logger.debug("createGalleryIdentifier");
		return new GalleryCollectionIdentifier(id);
	}

	@Override
	public void deleteCollection(final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
		try {
			logger.debug("deleteGallery");
			// delete all images of gallery
			final List<GalleryEntryIdentifier> images = getEntryIdentifiers(galleryIdentifier);
			for (final GalleryEntryIdentifier image : images) {
				galleryEntryDao.delete(image);
			}
			// delete gallery
			galleryCollectionDao.delete(galleryIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<GalleryCollectionIdentifier> getCollectionIdentifiers() throws GalleryServiceException {
		try {
			logger.debug("getGalleryIdentifiers");
			final List<GalleryCollectionIdentifier> result = new ArrayList<GalleryCollectionIdentifier>();
			final IdentifierIterator<GalleryCollectionIdentifier> i = galleryCollectionDao.getIdentifierIterator();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
		catch (final IdentifierIteratorException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryCollection getCollection(final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
		try {
			logger.debug("getGallery");
			return galleryCollectionDao.load(galleryIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<GalleryCollection> getCollections() throws GalleryServiceException {
		try {
			logger.debug("getGalleries");
			final EntityIterator<GalleryCollectionBean> i = galleryCollectionDao.getEntityIterator();
			final List<GalleryCollection> result = new ArrayList<GalleryCollection>();
			while (i.hasNext()) {
				result.add(i.next());
			}
			return result;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
		catch (final EntityIteratorException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryEntryIdentifier createEntry(final GalleryCollectionIdentifier galleryCollectionIdentifier, final String entryName) throws GalleryServiceException {
		try {
			logger.debug("saveImage");
			final GalleryEntryBean image = new GalleryEntryBean();
			final GalleryEntryIdentifier id = createEntryIdentifier(idGeneratorUUID.nextId());
			image.setId(id);
			image.setGalleryIdentifier(galleryCollectionIdentifier);
			image.setName(entryName);
			galleryEntryDao.save(image);
			logger.debug("saveImage name: " + entryName);
			return id;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryImageIdentifier createImageIdentifier(final String id) throws GalleryServiceException {
		return new GalleryImageIdentifier(id);
	}

	@Override
	public GalleryImage getImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		try {
			return galleryImageDao.load(id);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}
}
