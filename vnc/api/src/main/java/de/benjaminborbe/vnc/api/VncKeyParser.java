package de.benjaminborbe.vnc.api;

import java.util.List;

public interface VncKeyParser {

	VncKey parseKey(final char c) throws VncKeyParseException;

	List<VncKey> parseKeys(final String keyString) throws VncKeyParseException;

}
