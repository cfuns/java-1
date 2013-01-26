package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;

public interface JSONArray extends JSONElement, Iterable<Object> {

	JSONArray add(Object value);

	void writeJSONString(Writer out) throws IOException;

	int size();

	Object get(int i);
}
