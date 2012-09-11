package de.benjaminborbe.tools.map;

import java.util.HashMap;
import java.util.Map;

public class MapChain<A, B> extends HashMap<A, B> implements Map<A, B> {

	private static final long serialVersionUID = -2502606437904228403L;

	public MapChain<A, B> add(final A key, final B value) {
		this.put(key, value);
		return this;
	}
}
