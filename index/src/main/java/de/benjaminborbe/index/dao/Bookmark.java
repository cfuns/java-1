package de.benjaminborbe.index.dao;

public class Bookmark implements Entity {

	private static final long serialVersionUID = 6058606350883201939L;

	private Long id;

	private String name;

	private String url;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
