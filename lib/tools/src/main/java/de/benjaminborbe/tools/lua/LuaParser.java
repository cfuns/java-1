package de.benjaminborbe.tools.lua;

import java.io.StringWriter;

public interface LuaParser {

	LuaHash parse(final StringWriter luaContent) throws LuaParseException;

	LuaHash parse(final String luaContent) throws LuaParseException;
}
