package de.benjaminborbe.wiki.render.part;

public class Head1Part implements Part {

	private final String title;

	public Head1Part(final String title) {
		this.title = title;
	}

	@Override
	public String asString() {
		return "<h1><a name=\"" + title + "\"></a>" + title + "</h1>";
	}
}
