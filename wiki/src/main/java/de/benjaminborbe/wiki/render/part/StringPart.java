package de.benjaminborbe.wiki.render.part;

public class StringPart implements Part {

	private final String content;

	public StringPart(final String content) {
		this.content = content;
	}

	@Override
	public String asString() {
		return content;
	}

}
