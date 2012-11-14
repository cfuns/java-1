package de.benjaminborbe.tools.url;

import java.util.Collection;

import de.benjaminborbe.tools.map.MapChain;

public class MapParameter extends MapChain<String, String[]> {

	private static final long serialVersionUID = 7577476284489842653L;

	public MapParameter add(final String key, final String value) {
		add(key, new String[] { value });
		return this;
	}

	@Override
	public MapParameter add(final String key, final String[] values) {
		super.add(key, values);
		return this;
	}

	public MapParameter add(final String key, final Collection<String> values) {
		final String[] list = values.toArray(new String[values.size()]);
		add(key, list);
		return this;
	}

}
