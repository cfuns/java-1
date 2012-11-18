package de.benjaminborbe.gallery.dao;

import java.util.Calendar;

import de.benjaminborbe.gallery.api.GalleryCollectionIdentifier;
import de.benjaminborbe.gallery.api.GalleryEntry;
import de.benjaminborbe.gallery.api.GalleryEntryIdentifier;
import de.benjaminborbe.gallery.api.GalleryImageIdentifier;
import de.benjaminborbe.storage.tools.Entity;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

public class GalleryEntryBean implements Entity<GalleryEntryIdentifier>, GalleryEntry, HasCreated, HasModified {

	private static final long serialVersionUID = 6353074828349973344L;

	private GalleryEntryIdentifier id;

	private String name;

	private GalleryCollectionIdentifier galleryIdentifier;

	private Calendar created;

	private Calendar modified;

	private GalleryImageIdentifier previewImageIdentifier;

	private GalleryImageIdentifier imageIdentifier;

	private Long priority;

	@Override
	public GalleryEntryIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final GalleryEntryIdentifier id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public GalleryCollectionIdentifier getGalleryIdentifier() {
		return galleryIdentifier;
	}

	public void setGalleryIdentifier(final GalleryCollectionIdentifier galleryIdentifier) {
		this.galleryIdentifier = galleryIdentifier;
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
	public GalleryImageIdentifier getImageIdentifier() {
		return imageIdentifier;
	}

	@Override
	public GalleryImageIdentifier getPreviewImageIdentifier() {
		return previewImageIdentifier;
	}

	public void setPreviewImageIdentifier(final GalleryImageIdentifier previewImageIdentifier) {
		this.previewImageIdentifier = previewImageIdentifier;
	}

	public void setImageIdentifier(final GalleryImageIdentifier imageIdentifier) {
		this.imageIdentifier = imageIdentifier;
	}

	@Override
	public Long getPriority() {
		return priority;
	}

	public void setPriority(final Long priority) {
		this.priority = priority;
	}

}
