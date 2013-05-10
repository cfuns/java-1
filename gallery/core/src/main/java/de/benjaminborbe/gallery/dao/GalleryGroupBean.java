package de.benjaminborbe.gallery.dao;

import de.benjaminborbe.gallery.api.GalleryGroup;
import de.benjaminborbe.gallery.api.GalleryGroupIdentifier;
import de.benjaminborbe.gallery.util.HasShared;
import de.benjaminborbe.storage.tools.EntityBase;
import de.benjaminborbe.storage.tools.HasCreated;
import de.benjaminborbe.storage.tools.HasModified;

import java.util.Calendar;

public class GalleryGroupBean extends EntityBase<GalleryGroupIdentifier> implements GalleryGroup, HasCreated, HasModified, HasShared {

	private static final long serialVersionUID = -8803301003126328406L;

	private GalleryGroupIdentifier id;

	private String name;

	private Calendar created;

	private Calendar modified;

	private Boolean shared;

	private Integer shortSideMinLength;

	private Integer shortSideMaxLength;

	private Integer longSideMinLength;

	private Integer longSideMaxLength;

	private Integer previewShortSideMinLength;

	private Integer previewShortSideMaxLength;

	private Integer previewLongSideMinLength;

	private Integer previewLongSideMaxLength;

	public Integer getPreviewLongSideMaxLength() {
		return previewLongSideMaxLength;
	}

	public void setPreviewLongSideMaxLength(final Integer previewLongSideMaxLength) {
		this.previewLongSideMaxLength = previewLongSideMaxLength;
	}

	public Integer getPreviewLongSideMinLength() {
		return previewLongSideMinLength;
	}

	public void setPreviewLongSideMinLength(final Integer previewLongSideMinLength) {
		this.previewLongSideMinLength = previewLongSideMinLength;
	}

	public Integer getPreviewShortSideMaxLength() {
		return previewShortSideMaxLength;
	}

	public void setPreviewShortSideMaxLength(final Integer previewShortSideMaxLength) {
		this.previewShortSideMaxLength = previewShortSideMaxLength;
	}

	public Integer getPreviewShortSideMinLength() {
		return previewShortSideMinLength;
	}

	public void setPreviewShortSideMinLength(final Integer previewShortSideMinLength) {
		this.previewShortSideMinLength = previewShortSideMinLength;
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

	@Override
	public Boolean getShared() {
		return shared;
	}

	public void setShared(final Boolean shared) {
		this.shared = shared;
	}

	@Override
	public GalleryGroupIdentifier getId() {
		return id;
	}

	@Override
	public void setId(final GalleryGroupIdentifier id) {
		this.id = id;
	}

}
