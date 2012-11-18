package de.benjaminborbe.gallery.api;

import de.benjaminborbe.authentication.api.SessionIdentifier;

public interface GalleryService extends GalleryServiceGroup, GalleryServiceCollection, GalleryServiceEntry, GalleryServiceImage {

	GalleryCollectionIdentifier getCollectionIdentifierByName(SessionIdentifier sessionIdentifier, String name) throws GalleryServiceException;

}
