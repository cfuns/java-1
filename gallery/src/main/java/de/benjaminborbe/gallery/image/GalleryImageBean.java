package de.benjaminborbe.gallery.image;

import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class GalleryImageBean implements Entity<GalleryImageIdentifier>, GalleryImage {

	private static final long serialVersionUID = 6353074828349973344L;

	private String name;

	private byte[] content;

	private GalleryImageIdentifier id;

	private String contentType;

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public byte[] getContent() {
		return content;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}

	@Override
	public void setId(final GalleryImageIdentifier id) {
		this.id = id;
	}

	@Override
	public GalleryImageIdentifier getId() {
		return id;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

}
