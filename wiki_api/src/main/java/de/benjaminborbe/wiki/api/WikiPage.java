package de.benjaminborbe.wiki.api;

public interface WikiPage {

	WikiPageIdentifier getId();

	String getTitle();

	String getContent();

}
