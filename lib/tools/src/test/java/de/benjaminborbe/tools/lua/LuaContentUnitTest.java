package de.benjaminborbe.tools.lua;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LuaContentUnitTest {

	@Test
	public void testEmpty() {
		final LuaContent luaContent = new LuaContent("");
		assertFalse(luaContent.hasNext());
	}

	@Test
	public void testOne() {
		final LuaContent luaContent = new LuaContent("a");
		assertTrue(luaContent.hasNext());
		luaContent.next();
		assertFalse(luaContent.hasNext());
		assertEquals(0, luaContent.getCurrentPos());
		assertEquals('a', luaContent.getCurrentChar());
	}

	@Test
	public void testSubstring() {
		final LuaContent luaContent = new LuaContent("a");
		assertEquals("a", luaContent.substring(0, 1));
	}
}
