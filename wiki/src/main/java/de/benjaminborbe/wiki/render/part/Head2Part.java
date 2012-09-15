package de.benjaminborbe.wiki.render.part;

public class Head2Part implements Part {

	private final String title;

	private final String head1;

	public Head2Part(final String head1, final String title) {
		this.head1 = head1;
		this.title = title;
	}

	@Override
	public String asString() {
		return "<h2><a name=\"" + head1 + "-" + title + "\"></a>" + title + "</h2>";
	}
}
