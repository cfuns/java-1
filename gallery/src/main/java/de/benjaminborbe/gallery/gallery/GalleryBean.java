package de.benjaminborbe.gallery.gallery;

import de.benjaminborbe.gallery.api.Gallery;
import de.benjaminborbe.gallery.api.GalleryIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class GalleryBean implements Entity<GalleryIdentifier>, Gallery {

	private static final long serialVersionUID = -8803301003126328406L;

	private GalleryIdentifier id;

	private String name;

	@Override
	public GalleryIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final GalleryIdentifier id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

}
