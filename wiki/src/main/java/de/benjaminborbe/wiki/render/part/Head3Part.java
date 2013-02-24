package de.benjaminborbe.wiki.render.part;

public class Head3Part implements Part {

	private final String title;

	private final String head1;

	private final String head2;

	public Head3Part(final String head1, final String head2, final String title) {
		this.head1 = head1;
		this.head2 = head2;
		this.title = title;
	}

	@Override
	public String asString() {
		return "<h3 class=\"wikiH3\"><a name=\"" + head1 + "-" + head2 + "-" + title + "\"></a>" + title + "</h3>";
	}

	public String getTitle() {
		return title;
	}

}
