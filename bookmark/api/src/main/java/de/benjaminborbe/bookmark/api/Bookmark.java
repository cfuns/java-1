package de.benjaminborbe.bookmark.api;

import java.util.List;

public interface Bookmark {

	BookmarkIdentifier getId();

	String getName();

	String getUrl();

	String getDescription();

	List<String> getKeywords();

}
