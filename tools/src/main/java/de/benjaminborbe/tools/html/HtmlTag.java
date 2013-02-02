package de.benjaminborbe.tools.html;

import java.util.HashMap;
import java.util.Map;

public class HtmlTag {

	private final String name;

	private final Map<String, String> attributes = new HashMap<String, String>();

	public HtmlTag(final String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public HtmlTag addAttribute(final String key, final String value) {
		attributes.put(key, value);
		return this;
	}

}
