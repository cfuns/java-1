package de.benjaminborbe.blog.gui.atom;

public class EntryBean implements Entry {

	private String title;

	private String link;

	private String id;

	private String published;

	private String updated;

	private String summary;

	private String content;

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getLink() {
		return link;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getPublished() {
		return published;
	}

	@Override
	public String getUpdated() {
		return updated;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public String getContent() {
		return content;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public void setLink(final String link) {
		this.link = link;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public void setPublished(final String published) {
		this.published = published;
	}

	public void setUpdated(final String updated) {
		this.updated = updated;
	}

	public void setSummary(final String summary) {
		this.summary = summary;
	}

	public void setContent(final String content) {
		this.content = content;
	}
}
