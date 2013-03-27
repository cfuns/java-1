package de.benjaminborbe.tools.lua;

public class LuaValue implements Lua {

	private final String value;

	public LuaValue(final String value) {
		this.value = value;
	}

	@Override
	public boolean isValue() {
		return true;
	}

	@Override
	public boolean isHash() {
		return false;
	}

	@Override
	public boolean isArray() {
		return false;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

}
