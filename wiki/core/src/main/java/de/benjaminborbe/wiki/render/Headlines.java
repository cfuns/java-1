package de.benjaminborbe.wiki.render;

import de.benjaminborbe.wiki.render.part.Head1Part;
import de.benjaminborbe.wiki.render.part.Head2Part;
import de.benjaminborbe.wiki.render.part.Head3Part;
import de.benjaminborbe.wiki.render.part.Part;
import de.benjaminborbe.wiki.render.part.TocPart;

import java.util.ArrayList;
import java.util.List;

public class Headlines {

	private final List<Head1Part> h1s = new ArrayList<Head1Part>();

	public Part createHead1Part(final String title) {
		final Head1Part p = new Head1Part(title);
		h1s.add(p);
		return p;
	}

	public Part createHead2Part(final String title) {
		final Head1Part h1 = getLastH1();
		final Head2Part h2 = new Head2Part(h1.getTitle(), title);
		h1.addHead2Part(h2);
		return h2;
	}

	public Part createHead3Part(final String title) {
		final Head1Part h1 = getLastH1();
		final Head2Part h2 = getLastH2();
		final Head3Part h3 = new Head3Part(h1.getTitle(), h2.getTitle(), title);
		h2.addHead3Part(h3);
		return h3;
	}

	public Part createTocPart() {
		return new TocPart(h1s);
	}

	private Head1Part getLastH1() {
		if (h1s.isEmpty()) {
			h1s.add(new Head1Part("none"));
		}
		return h1s.get(h1s.size() - 1);
	}

	private Head2Part getLastH2() {
		final Head1Part h1 = getLastH1();
		final List<Head2Part> h2s = h1.getHead2Parts();
		if (h2s.isEmpty()) {
			h2s.add(new Head2Part(h1.getTitle(), "none"));
		}
		return h2s.get(h2s.size() - 1);
	}

}
