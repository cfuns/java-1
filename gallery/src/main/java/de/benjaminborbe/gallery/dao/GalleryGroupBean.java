package de.benjaminborbe.gallery.dao;

import java.util.Calendar;

import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class GalleryGroupBean implements Entity<GalleryGroupIdentifier>, GalleryGroup, HasCreated, HasModified {

	private static final long serialVersionUID = -8803301003126328406L;

	private GalleryGroupIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	@Override
	public GalleryGroupIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final GalleryGroupIdentifier id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
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

}
