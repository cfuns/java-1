package de.benjaminborbe.tools.html;

import java.util.HashMap;
import java.util.Map;

public class HtmlTag {

	private final String name;

	private final Map<String, String> attributes = new HashMap<String, String>();

	private final boolean opening;

	private final boolean closing;

	public HtmlTag(final String name, final boolean opening, final boolean closing) {
		this.name = name;
		this.opening = opening;
		this.closing = closing;
	}

	public String getName() {
		return name;
	}

	public HtmlTag addAttribute(final String key, final String value) {
		attributes.put(key, value);
		return this;
	}

	public String getAttribute(final String name) {
		return attributes.get(name);
	}

	public boolean isOpening() {
		return opening;
	}

	public boolean isClosing() {
		return closing;
	}

}
