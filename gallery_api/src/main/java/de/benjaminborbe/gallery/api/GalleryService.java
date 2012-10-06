package de.benjaminborbe.gallery.api;

import java.util.Collection;
import java.util.List;

public interface GalleryService {

	GalleryImageIdentifier saveImage(GalleryIdentifier galleryIdentifier, String imageName, String imageContentType, byte[] imageContent) throws GalleryServiceException;

	List<GalleryImageIdentifier> getImages(GalleryIdentifier galleryIdentifier) throws GalleryServiceException;

	GalleryImage getImage(GalleryImageIdentifier id) throws GalleryServiceException;

	void deleteImage(GalleryImageIdentifier id) throws GalleryServiceException;

	GalleryImageIdentifier createGalleryImageIdentifier(String id) throws GalleryServiceException;

	GalleryIdentifier createGallery(String name) throws GalleryServiceException;

	void deleteGallery(GalleryIdentifier galleryIdentifier) throws GalleryServiceException;

	Collection<GalleryIdentifier> getGalleries() throws GalleryServiceException;

	GalleryIdentifier createGalleryIdentifier(String id) throws GalleryServiceException;

	Gallery getGallery(GalleryIdentifier galleryIdentifier) throws GalleryServiceException;
}
