package de.benjaminborbe.gallery.api;

import java.util.Calendar;

public interface GalleryGroup {

	GalleryGroupIdentifier getId();

	String getName();

	Calendar getCreated();

	Calendar getModified();

	Boolean getShared();

	Integer getLongSideMaxLength();

	Integer getLongSideMinLength();

	Integer getShortSideMaxLength();

	Integer getShortSideMinLength();

	Integer getPreviewLongSideMaxLength();

	Integer getPreviewLongSideMinLength();

	Integer getPreviewShortSideMaxLength();

	Integer getPreviewShortSideMinLength();
}
