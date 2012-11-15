package de.benjaminborbe.gallery.api;

import java.util.List;

public interface GalleryServiceEntry {

	GalleryEntryIdentifier createEntryIdentifier(String id) throws GalleryServiceException;

	GalleryEntryIdentifier createEntry(GalleryCollectionIdentifier galleryCollectionIdentifier, String entryName) throws GalleryServiceException;

	List<GalleryEntryIdentifier> getEntryIdentifiers(GalleryCollectionIdentifier galleryCollectionIdentifier) throws GalleryServiceException;

	GalleryEntry getEntry(GalleryEntryIdentifier id) throws GalleryServiceException;

	void deleteEntry(GalleryEntryIdentifier id) throws GalleryServiceException;

}
