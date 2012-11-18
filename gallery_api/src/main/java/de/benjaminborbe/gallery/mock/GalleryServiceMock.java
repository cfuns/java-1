package de.benjaminborbe.gallery.mock;

import java.util.Collection;
import java.util.List;

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

@Singleton
public class GalleryServiceMock implements GalleryService {

	@Inject
	public GalleryServiceMock() {
	}

	@Override
	public void deleteCollection(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException {
	}

	@Override
	public Collection<GalleryCollectionIdentifier> getCollectionIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public Collection<GalleryCollection> getCollections(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryCollectionIdentifier createCollectionIdentifier(final String id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryCollection getCollection(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryEntryIdentifier createEntryIdentifier(final String id) throws GalleryServiceException {
		return null;
	}

	@Override
	public List<GalleryEntryIdentifier> getEntryIdentifiers(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryCollectionIdentifier)
			throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryEntry getEntry(final SessionIdentifier sessionIdentifier, final GalleryEntryIdentifier id) throws GalleryServiceException {
		return null;
	}

	@Override
	public void deleteEntry(final SessionIdentifier sessionIdentifier, final GalleryEntryIdentifier id) throws GalleryServiceException {
	}

	@Override
	public GalleryImageIdentifier createImageIdentifier(final String id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryImage getImage(final SessionIdentifier sessionIdentifier, final GalleryImageIdentifier id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryCollectionIdentifier createCollection(final SessionIdentifier sessionIdentifier, final GalleryGroupIdentifier galleryGroupIdentifier, final String collectionName,
			final Long prio) throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public GalleryEntryIdentifier createEntry(final SessionIdentifier sessionIdentifier, final GalleryCollectionIdentifier galleryCollectionIdentifier, final String entryName,
			final Long priority, final String imagePreviewName, final byte[] imagePreviewContent, final String imagePreviewContentType, final String imageName,
			final byte[] imageContent, final String imageContentType) throws GalleryServiceException, LoginRequiredException, PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public void deleteGroup(final SessionIdentifier sessionIdentifier, final GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException {
	}

	@Override
	public GalleryGroupIdentifier createGroup(final SessionIdentifier sessionIdentifier, final String groupName) throws GalleryServiceException, LoginRequiredException,
			PermissionDeniedException, ValidationException {
		return null;
	}

	@Override
	public Collection<GalleryGroupIdentifier> getGroupIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public Collection<GalleryGroup> getGroups(final SessionIdentifier sessionIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryGroupIdentifier createGroupIdentifier(final String id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryGroup getGroup(final SessionIdentifier sessionIdentifier, final GalleryGroupIdentifier galleryGroupIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryGroupIdentifier getGroupByName(final SessionIdentifier sessionIdentifier, final String groupName) throws GalleryServiceException {
		return null;
	}

	@Override
	public Collection<GalleryCollection> getCollectionsWithGroup(final SessionIdentifier sessionIdentifier, final GalleryGroupIdentifier galleryGroupIdentifier)
			throws GalleryServiceException {
		return null;
	}
}
