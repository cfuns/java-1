package de.benjaminborbe.blog.gui.atom;

import java.util.List;

public interface Feed {

	String getId();

	String getTitle();

	String getSubtitle();

	Author getAuthor();

	String getLink();

	String getUpdated();

	List<Entry> getEntries();

}
