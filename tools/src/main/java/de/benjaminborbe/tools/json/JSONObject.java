package de.benjaminborbe.tools.json;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface JSONObject extends Map<String, Object>, JSONElement {

	void writeJSONString(Writer out) throws IOException;

}
