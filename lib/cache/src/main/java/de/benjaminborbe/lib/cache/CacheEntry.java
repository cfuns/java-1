package de.benjaminborbe.lib.cache;

public class CacheEntry<Value> {

	private final Value value;

	private final long expire;

	public CacheEntry(Value value, long expire) {
		this.value = value;
		this.expire = expire;
	}

	public long getExpire() {
		return expire;
	}

	public Value getValue() {
		return value;
	}
}
