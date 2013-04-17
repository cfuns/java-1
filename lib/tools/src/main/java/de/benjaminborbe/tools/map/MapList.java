package de.benjaminborbe.tools.map;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapList<K, V> {

	private final Map<K, List<V>> data = new HashMap<>();

	public void add(final K key, final V value) {
		get(key).add(value);
	}

	public List<V> get(final K key) {
		if (!data.containsKey(key)) {
			data.put(key, new ArrayList<V>());
		}
		return data.get(key);
	}

	public Collection<K> keySet() {
		return data.keySet();
	}

	public Set<Entry<K, List<V>>> entrySet() {
		return data.entrySet();
	}

	public Collection<List<V>> values() {
		return data.values();
	}

	public void remove(final K key, final V value) {
		get(key).remove(value);
	}
}
