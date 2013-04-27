package de.benjaminborbe.tools.url;

import de.benjaminborbe.tools.map.MapChain;

import java.util.Collection;

public class MapParameter extends MapChain<String, String[]> {

	private static final long serialVersionUID = 7577476284489842653L;

	public MapParameter add(final String key, final Object value) {
		if (value == null) {
			add(key, new String[0]);
		} else {
			add(key, new String[]{String.valueOf(value)});
		}
		return this;
	}

	public MapParameter add(final String key, final String value) {
		if (value == null) {
			add(key, new String[0]);
		} else {
			add(key, new String[]{value});
		}
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
