package de.benjaminborbe.tools.lua;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LuaParserUnitTest {

	@Test
	public void testParse() throws LuaParseException {
		final StringWriter luaContent = new StringWriter();
		final LuaParser luaParse = new LuaParserImpl();
		final LuaHash lua = luaParse.parse(luaContent);
		assertNotNull(lua);
		assertEquals(0, lua.keys().size());
	}

	@Test
	public void testParseSimpleQuote() throws LuaParseException {
		final StringWriter luaContent = new StringWriter();
		luaContent.append("\"KEY\" = VALUE\n");
		luaContent.append("\"MY KEY\" = \"MY VALUE\"\n");
		final LuaParser luaParse = new LuaParserImpl();
		final LuaHash lua = luaParse.parse(luaContent);
		assertNotNull(lua);
		assertEquals(2, lua.keys().size());
		assertEquals("VALUE", String.valueOf(lua.get("KEY")));
		assertEquals("MY VALUE", String.valueOf(lua.get("MY KEY")));
	}

	@Test
	public void testParseSimple() throws LuaParseException {
		final StringWriter luaContent = new StringWriter();
		luaContent.append("KEY = VALUE\n");
		luaContent.append("DEC = 42\n");
		luaContent.append("ZERO = 0\n");
		luaContent.append("FLOAT = 13.37\n");
		luaContent.append("NEG = -9\n");
		final LuaParser luaParse = new LuaParserImpl();
		final LuaHash lua = luaParse.parse(luaContent);
		assertNotNull(lua);
		assertEquals(5, lua.keys().size());
		assertEquals("VALUE", String.valueOf(lua.get("KEY")));
		assertEquals("42", String.valueOf(lua.get("DEC")));
		assertEquals("0", String.valueOf(lua.get("ZERO")));
		assertEquals("13.37", String.valueOf(lua.get("FLOAT")));
		assertEquals("-9", String.valueOf(lua.get("NEG")));
	}

	@Test
	public void testParseSimpleHash() throws LuaParseException {
		final StringWriter luaContent = new StringWriter();
		luaContent.append("A = 1\n");
		luaContent.append("B = 2\n");
		luaContent.append("C = 3\n");
		final LuaParser luaParse = new LuaParserImpl();
		final LuaHash lua = luaParse.parse(luaContent);
		assertNotNull(lua);
		assertEquals(3, lua.keys().size());
		assertEquals("1", String.valueOf(lua.get("A")));
		assertEquals("2", String.valueOf(lua.get("B")));
		assertEquals("3", String.valueOf(lua.get("C")));
	}

	@Test
	public void testParseHash() throws LuaParseException {
		final StringWriter luaContent = new StringWriter();
		luaContent.append("A = {\n");
		luaContent.append("A1 = V1\n");
		luaContent.append("A2 = V2\n");
		luaContent.append("A3 = V3\n");
		luaContent.append("}\n");
		luaContent.append("B = 1\n");
		luaContent.append("C = 2\n");
		final LuaParser luaParse = new LuaParserImpl();
		final LuaHash lua = luaParse.parse(luaContent);
		assertNotNull(lua);
		assertEquals(3, lua.keys().size());
		assertTrue(lua.get("A").isHash());
		assertEquals("1", String.valueOf(lua.get("B")));
		assertEquals("2", String.valueOf(lua.get("C")));
	}
}
