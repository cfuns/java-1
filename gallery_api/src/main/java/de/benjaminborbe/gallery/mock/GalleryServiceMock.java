package de.benjaminborbe.gallery.mock;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

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
	public List<GalleryImageIdentifier> getImages() throws GalleryServiceException {
		return null;
	}

	@Override
	public void deleteImage(final GalleryImageIdentifier id) throws GalleryServiceException {
	}

	@Override
	public GalleryImageIdentifier createGalleryImageIdentifier(final String id) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryImageIdentifier saveImage(final String imageName, final String imageContentType, final byte[] imageContent) throws GalleryServiceException {
		return null;
	}

	@Override
	public GalleryImage getImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		return null;
	}

}
