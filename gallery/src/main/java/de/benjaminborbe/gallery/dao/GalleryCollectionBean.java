package de.benjaminborbe.gallery.dao;

import java.util.Calendar;

import de.benjaminborbe.gallery.api.GalleryCollection;
import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.storage.tools.Entity;

public class GalleryCollectionBean implements Entity<GalleryCollectionIdentifier>, GalleryCollection {

	private static final long serialVersionUID = -8803301003126328406L;

	private GalleryCollectionIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	@Override
	public GalleryCollectionIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final GalleryCollectionIdentifier id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(final Calendar created) {
		this.created = created;
	}

	public Calendar getModified() {
		return modified;
	}

	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

}
