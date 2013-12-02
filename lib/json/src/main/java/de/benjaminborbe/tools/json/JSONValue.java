package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;

public interface JSONValue extends JSONElement {

	void writeJSONString(Writer out) throws IOException;
}
