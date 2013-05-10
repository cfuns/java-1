package de.benjaminborbe.gallery.api;

import java.util.Calendar;

public class GalleryGroupDto implements GalleryGroup {

	private GalleryGroupIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	private Boolean shared;

	private Integer shortSideMinLength;

	private Integer shortSideMaxLength;

	private Integer longSideMinLength;

	private Integer longSideMaxLength;

	public Calendar getCreated() {
		return created;
	}

	public void setCreated(final Calendar created) {
		this.created = created;
	}

	public GalleryGroupIdentifier getId() {
		return id;
	}

	public void setId(final GalleryGroupIdentifier id) {
		this.id = id;
	}

	public Integer getLongSideMaxLength() {
		return longSideMaxLength;
	}

	public void setLongSideMaxLength(final Integer longSideMaxLength) {
		this.longSideMaxLength = longSideMaxLength;
	}

	public Integer getLongSideMinLength() {
		return longSideMinLength;
	}

	public void setLongSideMinLength(final Integer longSideMinLength) {
		this.longSideMinLength = longSideMinLength;
	}

	public Calendar getModified() {
		return modified;
	}

	public void setModified(final Calendar modified) {
		this.modified = modified;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Boolean getShared() {
		return shared;
	}

	public void setShared(final Boolean shared) {
		this.shared = shared;
	}

	public Integer getShortSideMaxLength() {
		return shortSideMaxLength;
	}

	public void setShortSideMaxLength(final Integer shortSideMaxLength) {
		this.shortSideMaxLength = shortSideMaxLength;
	}

	public Integer getShortSideMinLength() {
		return shortSideMinLength;
	}

	public void setShortSideMinLength(final Integer shortSideMinLength) {
		this.shortSideMinLength = shortSideMinLength;
	}
}
