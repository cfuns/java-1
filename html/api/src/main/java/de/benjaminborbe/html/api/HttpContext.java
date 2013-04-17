package de.benjaminborbe.html.api;

import java.util.HashMap;
import java.util.Map;

public class HttpContext {

	private final Map<String, String> data = new HashMap<>();

	public Map<String, String> getData() {
		return data;
	}

}
