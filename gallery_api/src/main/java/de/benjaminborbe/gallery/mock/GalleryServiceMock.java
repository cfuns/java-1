package de.benjaminborbe.gallery.mock;

import java.util.Collection;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.gallery.api.GalleryIdentifier;
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
	public void deleteImage(final GalleryImageIdentifier id) throws GalleryServiceException {
	}

	@Override
	public GalleryImageIdentifier createGalleryImageIdentifier(final String id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryImage getImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryIdentifier createGallery(final String name) throws GalleryServiceException {
		return null;
	}

	@Override
	public void deleteGallery(final GalleryIdentifier galleryIdentifier) throws GalleryServiceException {
	}

	@Override
	public Collection<GalleryIdentifier> getGalleries() throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryIdentifier createGalleryIdentifier(final String id) {
		return null;
	}

	@Override
	public Gallery getGallery(final GalleryIdentifier galleryIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public List<GalleryImageIdentifier> getImages(final GalleryIdentifier galleryIdentifier) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryImageIdentifier saveImage(final GalleryIdentifier galleryIdentifier, final String imageName, final String imageContentType, final byte[] imageContent) throws GalleryServiceException {
		return null;
	}

}
