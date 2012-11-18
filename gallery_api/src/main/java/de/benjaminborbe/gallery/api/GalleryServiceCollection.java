package de.benjaminborbe.gallery.api;

import java.util.Collection;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface GalleryServiceCollection {

	Collection<GalleryCollection> getCollectionsWithEntries(final SessionIdentifier sessionIdentifier) throws GalleryServiceException;

	void deleteCollection(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException;

	GalleryCollectionIdentifier createCollection(final SessionIdentifier sessionIdentifier, String collectionName) throws GalleryServiceException;

	Collection<GalleryCollectionIdentifier> getCollectionIdentifiers(final SessionIdentifier sessionIdentifier) throws GalleryServiceException;

	Collection<GalleryCollection> getCollections(final SessionIdentifier sessionIdentifier) throws GalleryServiceException;

	GalleryCollectionIdentifier createCollectionIdentifier(String id) throws GalleryServiceException;

	GalleryCollection getCollection(final SessionIdentifier sessionIdentifier, GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException;

}
