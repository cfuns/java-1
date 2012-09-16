package de.benjaminborbe.blog.gui.atom;

import java.util.List;

public class FeedBean implements Feed {

	private String id;

	private String title;

	private Author author;

	private String link;

	private String updated;

	private List<Entry> entries;

	private String subtitle;

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Override
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(final Author author) {
		this.author = author;
	}

	@Override
	public String getLink() {
		return link;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	@Override
	public String getUpdated() {
		return updated;
	}

	public void setUpdated(final String updated) {
		this.updated = updated;
	}

	@Override
	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(final List<Entry> entries) {
		this.entries = entries;
	}

	public void setSubtitle(final String subtitle) {
		this.subtitle = subtitle;
	}

	@Override
	public String getSubtitle() {
		return subtitle;
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

}
