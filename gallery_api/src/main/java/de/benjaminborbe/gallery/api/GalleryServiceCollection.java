package de.benjaminborbe.gallery.api;

import java.util.Collection;

public interface GalleryServiceCollection {

	Collection<GalleryCollection> getCollectionsWithEntries() throws GalleryServiceException;

	void deleteCollection(GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException;

	GalleryCollectionIdentifier createCollection(String collectionName) throws GalleryServiceException;

	Collection<GalleryCollectionIdentifier> getCollectionIdentifiers() throws GalleryServiceException;

	Collection<GalleryCollection> getCollections() throws GalleryServiceException;

	GalleryCollectionIdentifier createCollectionIdentifier(String id) throws GalleryServiceException;

	GalleryCollection getCollection(GalleryCollectionIdentifier galleryIdentifier) throws GalleryServiceException;

}
