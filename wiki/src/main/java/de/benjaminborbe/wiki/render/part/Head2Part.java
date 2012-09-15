package de.benjaminborbe.wiki.render.part;

import java.util.ArrayList;
import java.util.List;

public class Head2Part implements Part {

	private final String title;

	private final String head1;

	private final List<Head3Part> h3s = new ArrayList<Head3Part>();

	public Head2Part(final String head1, final String title) {
		this.head1 = head1;
		this.title = title;
	}

	public void addHead3Part(final Head3Part h3) {
		h3s.add(h3);
	}

	public List<Head3Part> getHead3Parts() {
		return h3s;
	}

	@Override
	public String asString() {
		return "<h2><a name=\"" + head1 + "-" + title + "\"></a>" + title + "</h2>";
	}

	public String getTitle() {
		return title;
	}

}
