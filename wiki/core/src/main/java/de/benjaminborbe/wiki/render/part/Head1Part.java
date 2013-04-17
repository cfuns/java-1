package de.benjaminborbe.wiki.render.part;

import java.util.ArrayList;
import java.util.List;

public class Head1Part implements Part {

	private final String title;

	private final List<Head2Part> h2s = new ArrayList<>();

	public Head1Part(final String title) {
		this.title = title;
	}

	public void addHead2Part(final Head2Part h2) {
		h2s.add(h2);
	}

	public List<Head2Part> getHead2Parts() {
		return h2s;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public String asString() {
		return "<h1 class=\"wikiH1\"><a name=\"" + title + "\"></a>" + title + "</h1>";
	}
}
