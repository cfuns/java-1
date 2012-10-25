package de.benjaminborbe.tools.lua;

import java.io.StringWriter;
import com.google.inject.Inject;

public class LuaParserImpl implements LuaParser {

	private static final boolean DEBUG = false;

	@Inject
	public LuaParserImpl() {
	}

	@Override
	public LuaHash parse(final StringWriter luaContent) throws LuaParseException {
		return parse(luaContent.toString());
	}

	@Override
	public LuaHash parse(final String luaContent) throws LuaParseException {
		final LuaContent content = new LuaContent(luaContent);
		if (content.hasNext()) {
			content.next();
			return parseHash(content);
		}
		else {
			return new LuaHash();
		}
	}

	private LuaHash parseHash(final LuaContent content) throws LuaParseException {
		final LuaHash lua = new LuaHash();
		while (content.hasNext()) {
			parseSpacesAndNewlines(content);

			if (content.existsCurrent() && content.getCurrentChar() == '}') {
				debug(content.getCurrentChar());
				content.next();
				return lua;
			}

			final String key = parseKey(content);
			if (key != null && key.length() > 0) {
				// System.err.println("key: " + key);
				parseSpaces(content);
				parseEquals(content);
				parseSpaces(content);
				final Lua value = parseValue(content);
				lua.add(key, value);
				parseSpaces(content);
			}
		}
		return lua;
	}

	private void debug(final char c) {
		if (DEBUG)
			System.out.print(c);
	}

	private void parseSpacesAndNewlines(final LuaContent content) {
		while (content.existsCurrent() && (content.getCurrentChar() == '\n' || content.getCurrentChar() == ' ')) {
			debug(content.getCurrentChar());
			content.next();
		}
	}

	private String parseKey(final LuaContent content) {
		return parseWord(content);
	}

	private void parseEquals(final LuaContent content) throws LuaParseException {
		if (content.existsCurrent() && content.getCurrentChar() == '=') {
			debug(content.getCurrentChar());
			content.next();
		}
		else {
			throw new LuaParseException("char '=' not found");
		}
	}

	private Lua parseValue(final LuaContent content) throws LuaParseException {
		if (content.existsCurrent() && content.getCurrentChar() == '{') {
			debug(content.getCurrentChar());
			content.next();
			return parseHash(content);
		}
		else {
			return new LuaValue(parseWord(content));
		}
	}

	private String parseWord(final LuaContent content) {
		final int startpos = content.getCurrentPos();
		while (content.existsCurrent() && isLetterAndNumber(content.getCurrentChar())) {
			debug(content.getCurrentChar());
			content.next();
		}
		return content.substring(startpos, content.getCurrentPos() - startpos);
	}

	private boolean isLetter(final char c) {
		return 'a' <= c && c <= 'z' || 'A' <= c && c <= 'Z';
	}

	private boolean isLetterAndNumber(final char c) {
		return isLetter(c) || isNumber(c);
	}

	private boolean isNumber(final char c) {
		return '1' <= c && c <= '9';
	}

	private void parseSpaces(final LuaContent content) {
		while (content.existsCurrent() && content.getCurrentChar() == ' ') {
			debug(content.getCurrentChar());
			content.next();
		}
	}

}
