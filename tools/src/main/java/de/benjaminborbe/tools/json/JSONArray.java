package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface JSONArray extends JSONElement, List<Object> {

	void writeJSONString(Writer out) throws IOException;

}
