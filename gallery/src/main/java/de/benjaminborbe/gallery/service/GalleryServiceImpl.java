package de.benjaminborbe.gallery.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.gallery.api.GalleryService;
import de.benjaminborbe.gallery.api.GalleryServiceException;

@Singleton
public class GalleryServiceImpl implements GalleryService {

	private final Logger logger;

	private final Map<GalleryImageIdentifier, GalleryImageBean> images = new HashMap<GalleryImageIdentifier, GalleryImageBean>();

	@Inject
	public GalleryServiceImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public GalleryImageIdentifier saveImage(final String name, final String contentType, final byte[] content) throws GalleryServiceException {
		logger.debug("saveImage");
		final GalleryImageBean image = new GalleryImageBean();
		final GalleryImageIdentifier id = createGalleryImageIdentifier(content);
		image.setId(id);
		image.setName(name);
		image.setContentType(contentType);
		image.setContent(content);
		images.put(id, image);
		logger.debug("saveImage name: " + name + " length: " + content.length);
		return id;
	}

	@Override
	public List<GalleryImageIdentifier> getImages() throws GalleryServiceException {
		logger.debug("getImageNames");
		final List<GalleryImageIdentifier> result = new ArrayList<GalleryImageIdentifier>();
		for (final GalleryImageBean image : images.values()) {
			result.add(image.getId());
		}
		return result;
	}

	@Override
	public void deleteImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		images.remove(id);
	}

	protected GalleryImageIdentifier createGalleryImageIdentifier(final byte[] content) throws GalleryServiceException {
		return createGalleryImageIdentifier(String.valueOf(UUID.nameUUIDFromBytes(content)));
	}

	@Override
	public GalleryImageIdentifier createGalleryImageIdentifier(final String id) throws GalleryServiceException {
		return new GalleryImageIdentifier(id);
	}

	@Override
	public GalleryImage getImage(final GalleryImageIdentifier id) throws GalleryServiceException {
		logger.debug("getImage");
		final GalleryImageBean image = images.get(id);
		logger.debug("getImageContent name: " + image.getName() + " length: " + image.getContent().length);
		return image;
	}
}
