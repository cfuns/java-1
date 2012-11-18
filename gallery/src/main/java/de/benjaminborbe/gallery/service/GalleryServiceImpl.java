package de.benjaminborbe.gallery.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.api.ValidationException;
import de.benjaminborbe.authentication.api.LoginRequiredException;
import de.benjaminborbe.authentication.api.SessionIdentifier;
import de.benjaminborbe.authorization.api.PermissionDeniedException;
import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;
import de.benjaminborbe.gallery.dao.GalleryCollectionBean;
import de.benjaminborbe.gallery.dao.GalleryCollectionDao;
import de.benjaminborbe.gallery.dao.GalleryEntryBean;
import de.benjaminborbe.gallery.dao.GalleryEntryDao;
import de.benjaminborbe.gallery.dao.GalleryGroupBean;
import de.benjaminborbe.gallery.dao.GalleryGroupDao;
import de.benjaminborbe.gallery.dao.GalleryImageBean;
import de.benjaminborbe.gallery.dao.GalleryImageDao;
import de.benjaminborbe.storage.api.StorageException;
import de.benjaminborbe.storage.tools.EntityIterator;
import de.benjaminborbe.storage.tools.EntityIteratorException;
import de.benjaminborbe.storage.tools.IdentifierIterator;
import de.benjaminborbe.storage.tools.IdentifierIteratorException;
import de.benjaminborbe.tools.date.CalendarUtil;
import de.benjaminborbe.tools.util.IdGeneratorUUID;

@Singleton
public class GalleryServiceImpl implements GalleryService {

	private final Logger logger;

	private final GalleryEntryDao galleryEntryDao;

	private final GalleryCollectionDao galleryCollectionDao;

	private final GalleryImageDao galleryImageDao;

	private final IdGeneratorUUID idGeneratorUUID;

	private final CalendarUtil calendarUtil;

	private final GalleryGroupDao galleryGroupDao;

	@Inject
	public GalleryServiceImpl(
			final Logger logger,
			final GalleryCollectionDao galleryDao,
			final GalleryEntryDao galleryEntryDao,
			final IdGeneratorUUID idGeneratorUUID,
			final GalleryImageDao galleryImageDao,
			final GalleryGroupDao galleryGroupDao,
			final CalendarUtil calendarUtil) {
		this.logger = logger;
		this.galleryCollectionDao = galleryDao;
		this.galleryEntryDao = galleryEntryDao;
		this.idGeneratorUUID = idGeneratorUUID;
		this.galleryImageDao = galleryImageDao;
		this.galleryGroupDao = galleryGroupDao;
		this.calendarUtil = calendarUtil;
	}

	@Override
	public List<GalleryEntryIdentifier> getEntryIdentifiers(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryIdentifier)
			throws GalleryServiceException {
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
	public void deleteEntry(final SessionIdentifier sessionIdentifier, final GalleryEntryIdentifier id) throws GalleryServiceException {
		try {
			logger.debug("deleteImage - GalleryImageIdentifier " + id);

			final GalleryEntry entry = getEntry(sessionIdentifier, id);
			deleteImage(entry.getPreviewImageIdentifier());
			deleteImage(entry.getImageIdentifier());

			galleryEntryDao.delete(id);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	private void deleteImage(final GalleryImageIdentifier imageIdentifier) throws StorageException {
		galleryImageDao.delete(imageIdentifier);
	}

	@Override
	public GalleryEntryIdentifier createEntryIdentifier(final String id) throws GalleryServiceException {
		logger.debug("createGalleryImageIdentifier");
		return new GalleryEntryIdentifier(id);
	}

	@Override
	public GalleryEntry getEntry(final SessionIdentifier sessionIdentifier, final GalleryEntryIdentifier id) throws GalleryServiceException {
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
	public GalleryCollectionIdentifier createCollection(final SessionIdentifier sessionIdentifier, final GalleryGroupIdentifier galleryGroupIdentifier, final String name,
			final Long priority) throws GalleryServiceException {
		try {
			logger.debug("createGallery name: " + name);
			final GalleryCollectionIdentifier galleryIdentifier = createCollectionIdentifier(idGeneratorUUID.nextId());
			final GalleryCollectionBean collection = galleryCollectionDao.create();
			collection.setId(galleryIdentifier);
			collection.setGroupId(galleryGroupIdentifier);
			collection.setName(name);
			collection.setPriority(priority);
			galleryCollectionDao.save(collection);
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
	public void deleteCollection(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
		try {
			logger.debug("deleteGallery");
			// delete all images of gallery
			final List<GalleryEntryIdentifier> entries = getEntryIdentifiers(sessionIdentifier, galleryIdentifier);
			for (final GalleryEntryIdentifier entry : entries) {
				deleteEntry(sessionIdentifier, entry);
			}

			// delete gallery
			galleryCollectionDao.delete(galleryIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<GalleryCollectionIdentifier> getCollectionIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
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
	public GalleryCollection getCollection(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
		try {
			logger.debug("getGallery");
			return galleryCollectionDao.load(galleryIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<GalleryCollection> getCollections(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
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
	public GalleryImageIdentifier createImageIdentifier(final String id) throws GalleryServiceException {
		return new GalleryImageIdentifier(id);
	}

	@Override
	public GalleryImage getImage(final SessionIdentifier sessionIdentifier, final GalleryImageIdentifier id) throws GalleryServiceException {
		try {
			return galleryImageDao.load(id);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryEntryIdentifier createEntry(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryCollectionIdentifier, final String entryName,
			final Long priority, final String imagePreviewName, final byte[] imagePreviewContent, final String imagePreviewContentType, final String imageName,
			final byte[] imageContent, final String imageContentType) throws GalleryServiceException {
		try {

			final GalleryImageIdentifier imageIdentifier = createImage(imageName, imageContent, imageContentType);
			final GalleryImageIdentifier previewImageIdentifier = createImage(imagePreviewName, imagePreviewContent, imagePreviewContentType);

			logger.debug("createEntry");
			final GalleryEntryBean entry = galleryEntryDao.create();
			final GalleryEntryIdentifier id = createEntryIdentifier(idGeneratorUUID.nextId());
			entry.setId(id);
			entry.setCollectionId(galleryCollectionIdentifier);
			entry.setName(entryName);
			entry.setPreviewImageIdentifier(previewImageIdentifier);
			entry.setImageIdentifier(imageIdentifier);
			entry.setPriority(priority);
			galleryEntryDao.save(entry);
			logger.debug("createEntry name: " + entryName);
			return id;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	private GalleryImageIdentifier createImage(final String imageName, final byte[] imageContent, final String imageContentType) throws GalleryServiceException {
		try {
			logger.debug("createImage");
			final GalleryImageBean image = galleryImageDao.create();
			final GalleryImageIdentifier id = createImageIdentifier(idGeneratorUUID.nextId());
			image.setId(id);
			image.setContentType(imageContentType);
			image.setContent(imageContent);
			image.setName(imageName);
			image.setModified(calendarUtil.now());
			image.setCreated(calendarUtil.now());
			galleryImageDao.save(image);
			logger.debug("createEntry name: " + imageName);
			return id;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public void deleteGroup(final SessionIdentifier sessionIdentifier, final GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException {
		try {
			logger.debug("deleteGroup");

			// delete collections
			for (final GalleryCollection collection : getCollectionsWithGroup(sessionIdentifier, galleryGroupIdentifier)) {
				deleteCollection(sessionIdentifier, collection.getId());
			}

			// delete gallery
			galleryGroupDao.delete(galleryGroupIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryGroupIdentifier createGroup(final SessionIdentifier sessionIdentifier, final String name) throws GalleryServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		try {
			logger.debug("createGallery name: " + name);
			final GalleryGroupIdentifier galleryGroupIdentifier = createGroupIdentifier(idGeneratorUUID.nextId());
			final GalleryGroupBean group = galleryGroupDao.create();
			group.setId(galleryGroupIdentifier);
			group.setName(name);
			galleryGroupDao.save(group);
			return galleryGroupIdentifier;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<GalleryGroupIdentifier> getGroupIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		try {
			logger.debug("getGroupIdentifiers");
			final List<GalleryGroupIdentifier> result = new ArrayList<GalleryGroupIdentifier>();
			final IdentifierIterator<GalleryGroupIdentifier> i = galleryGroupDao.getIdentifierIterator();
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
	public Collection<GalleryGroup> getGroups(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		try {
			logger.debug("getGalleries");
			final EntityIterator<GalleryGroupBean> i = galleryGroupDao.getEntityIterator();
			final List<GalleryGroup> result = new ArrayList<GalleryGroup>();
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
	public GalleryGroupIdentifier createGroupIdentifier(final String id) throws GalleryServiceException {
		logger.debug("createGroupIdentifier");
		return new GalleryGroupIdentifier(id);

	}

	@Override
	public GalleryGroup getGroup(final SessionIdentifier sessionIdentifier, final GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException {
		try {
			logger.debug("getGroup");
			return galleryGroupDao.load(galleryGroupIdentifier);
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public GalleryGroupIdentifier getGroupByName(final SessionIdentifier sessionIdentifier, final String groupName) throws GalleryServiceException {
		try {
			logger.debug("getGroupByName: " + groupName);
			final EntityIterator<GalleryGroupBean> i = galleryGroupDao.getEntityIterator();
			while (i.hasNext()) {
				final GalleryGroupBean group = i.next();
				if (groupName.equals(group.getName())) {
					return group.getId();
				}
			}
			return null;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
		catch (final EntityIteratorException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}

	@Override
	public Collection<GalleryCollection> getCollectionsWithGroup(final SessionIdentifier sessionIdentifier, final GalleryGroupIdentifier galleryGroupIdentifier)
			throws GalleryServiceException {
		try {
			logger.debug("getCollectionsWithGroup");
			final EntityIterator<GalleryCollectionBean> i = galleryCollectionDao.getEntityIterator();
			final List<GalleryCollection> result = new ArrayList<GalleryCollection>();
			while (i.hasNext()) {
				final GalleryCollectionBean collection = i.next();
				if (galleryGroupIdentifier.equals(collection.getGroupId())) {
					result.add(collection);
				}
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
	public Collection<GalleryEntry> getEntries(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryCollectionIdentifier)
			throws GalleryServiceException {

		try {
			logger.debug("getCollectionsWithGroup");
			final EntityIterator<GalleryEntryBean> i = galleryEntryDao.getEntityIterator();
			final List<GalleryEntry> result = new ArrayList<GalleryEntry>();
			while (i.hasNext()) {
				final GalleryEntryBean entry = i.next();
				if (galleryCollectionIdentifier.equals(entry.getCollectionId())) {
					result.add(entry);
				}
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
	public GalleryCollectionIdentifier getCollectionIdentifierByName(final SessionIdentifier sessionIdentifier, final String name) throws GalleryServiceException {
		try {
			logger.debug("getCollectionIdentifierByName: " + name);
			final EntityIterator<GalleryCollectionBean> i = galleryCollectionDao.getEntityIterator();
			while (i.hasNext()) {
				final GalleryCollectionBean bean = i.next();
				if (name.equals(bean.getName())) {
					return bean.getId();
				}
			}
			return null;
		}
		catch (final StorageException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
		catch (final EntityIteratorException e) {
			throw new GalleryServiceException(e.getClass().getName(), e);
		}
	}
}
