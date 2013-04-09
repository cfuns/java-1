package de.benjaminborbe.blog.gui.atom;

public interface Entry {

	String getTitle();

	String getLink();

	String getId();

	String getPublished();

	String getUpdated();

	String getSummary();

	String getContent();

}
