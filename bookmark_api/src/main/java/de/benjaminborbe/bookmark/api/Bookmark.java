package de.benjaminborbe.bookmark.api;

import java.util.List;

public interface Bookmark {

	String getName();

	String getUrl();

	String getDescription();

	List<String> getKeywords();

}
