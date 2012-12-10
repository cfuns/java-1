package de.benjaminborbe.search.api;

public class SearchResultImpl implements SearchResult {

	private final String url;

	private final String title;

	private final String description;

	private final String type;

	private final int matchCounter;

	public SearchResultImpl(final String type, final int matchCounter, final String title, final String url, final String description) {
		this.type = trim(type);
		this.matchCounter = matchCounter;
		this.title = trim(title);
		this.url = trim(url);
		this.description = trim(description);
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public int getMatchCounter() {
		return matchCounter;
	}

	private String trim(final String text) {
		return text != null ? text.trim() : null;
	}

}
