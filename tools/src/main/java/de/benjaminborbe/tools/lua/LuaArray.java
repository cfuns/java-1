package de.benjaminborbe.tools.lua;

public class LuaArray implements Lua {

	@Override
	public boolean isValue() {
		return false;
	}

	@Override
	public boolean isHash() {
		return false;
	}

	@Override
	public boolean isArray() {
		return true;
	}

}
