package de.benjaminborbe.gallery.dao;

import java.util.Calendar;

import de.benjaminborbe.gallery.api.GalleryImage;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class GalleryImageBean implements Entity<GalleryImageIdentifier>, GalleryImage, HasCreated, HasModified {

	private static final long serialVersionUID = 6353074828349973344L;

	private byte[] content;

	private String contentType;

	private Calendar created;

	private Calendar modified;

	private GalleryImageIdentifier id;

	private String name;

	@Override
	public byte[] getContent() {
		return content;
	}

	public void setContent(final byte[] content) {
		this.content = content;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	public void setContentType(final String contentType) {
		this.contentType = contentType;
	}

	@Override
	public Calendar getCreated() {
		return created;
	}

	@Override
	public void setCreated(final Calendar created) {
		this.created = created;
	}

	@Override
	public Calendar getModified() {
		return modified;
	}

	@Override
	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	@Override
	public GalleryImageIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final GalleryImageIdentifier id) {
		this.id = id;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

}
