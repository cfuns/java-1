package de.benjaminborbe.tools.json;

public interface JSONParser {

	Object parse(String jsonString) throws JSONParseException;

}
