package de.benjaminborbe.wiki.render;

import de.benjaminborbe.wiki.render.part.Head1Part;
import de.benjaminborbe.wiki.render.part.Head2Part;
import de.benjaminborbe.wiki.render.part.Head3Part;
import de.benjaminborbe.wiki.render.part.Part;

public class Headlines {

	private String lastHead1 = "none";

	private String lastHead2 = "none";

	public Part createHead1Part(final String title) {
		lastHead1 = title;
		lastHead2 = "none";
		return new Head1Part(title);
	}

	public Part createHead2Part(final String title) {
		lastHead2 = title;
		return new Head2Part(lastHead1, title);
	}

	public Part createHead3Part(final String title) {
		return new Head3Part(lastHead1, lastHead2, title);
	}

	public Part createTocPart() {
		return null;
	}

}
