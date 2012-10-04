package de.benjaminborbe.gallery.api;

import java.util.List;

public interface GalleryService {

	GalleryImageIdentifier saveImage(String imageName, String imageContentType, byte[] imageContent) throws GalleryServiceException;

	List<GalleryImageIdentifier> getImages() throws GalleryServiceException;

	GalleryImage getImage(GalleryImageIdentifier id) throws GalleryServiceException;

	void deleteImage(GalleryImageIdentifier id) throws GalleryServiceException;

	GalleryImageIdentifier createGalleryImageIdentifier(String id) throws GalleryServiceException;

}
