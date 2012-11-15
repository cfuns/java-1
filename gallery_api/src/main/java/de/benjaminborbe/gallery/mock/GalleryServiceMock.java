package de.benjaminborbe.gallery.mock;

import java.util.Collection;
import java.util.List;

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

@Singleton
public class GalleryServiceMock implements GalleryService {

	@Inject
	public GalleryServiceMock() {
	}

	@Override
	public void deleteEntry(final GalleryEntryIdentifier id) throws GalleryServiceException {
	}

	@Override
	public GalleryEntryIdentifier createEntryIdentifier(final String id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryEntry getEntry(final GalleryEntryIdentifier id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryCollectionIdentifier createCollection(final String name) throws GalleryServiceException {
		return null;
	}

	@Override
	public void deleteCollection(final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
	}

	@Override
	public Collection<GalleryCollectionIdentifier> getCollectionIdentifiers() throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryCollectionIdentifier createCollectionIdentifier(final String id) {
		return null;
	}

	@Override
	public GalleryCollection getCollection(final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public List<GalleryEntryIdentifier> getEntryIdentifiers(final GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public Collection<GalleryCollection> getCollections() throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryEntryIdentifier createEntry(final GalleryCollectionIdentifier galleryCollectionIdentifier, final String entryName) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryImageIdentifier createImageIdentifier(final String id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryImage getImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		return null;
	}

}
