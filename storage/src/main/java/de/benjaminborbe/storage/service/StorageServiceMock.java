package de.benjaminborbe.storage.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.benjaminborbe.storage.api.StorageService;


public class StorageServiceMock implements StorageService {

	private final HashMap<String, HashMap<String, HashMap<String, String>>> storage = new HashMap<String, HashMap<String, HashMap<String, String>>>();

	@Override
	public String get(final String columnFamily, final String id, final String key) {
		final HashMap<String, HashMap<String, String>> cfData = storage.get(columnFamily);
		if (cfData == null)
			return null;
		final HashMap<String, String> idData = cfData.get(id);
		if (idData == null)
			return null;
		return idData.get(key);
	}

	@Override
	public void delete(final String columnFamily, final String id, final String key) {
		final HashMap<String, HashMap<String, String>> cfData = storage.get(columnFamily);
		if (cfData == null)
			return;
		final HashMap<String, String> idData = cfData.get(id);
		if (idData == null)
			return;
		idData.remove(key);
	}

	@Override
	public void set(final String columnFamily, final String id, final String key, final String value) {
		HashMap<String, HashMap<String, String>> cfData = storage.get(columnFamily);
		if (cfData == null) {
			cfData = new HashMap<String, HashMap<String, String>>();
			storage.put(columnFamily, cfData);
		}
		HashMap<String, String> idData = cfData.get(id);
		if (idData == null) {
			idData = new HashMap<String, String>();
			cfData.put(id, idData);
		}
		idData.put(key, value);
	}

	@Override
	public void set(final String columnFamily, final String id, final Map<String, String> data) {
		HashMap<String, HashMap<String, String>> cfData = storage.get(columnFamily);
		if (cfData == null) {
			cfData = new HashMap<String, HashMap<String, String>>();
			storage.put(columnFamily, cfData);
		}
		final HashMap<String, String> idData = new HashMap<String, String>();
		cfData.put(id, idData);
		for (final Entry<String, String> e : data.entrySet()) {
			idData.put(e.getKey(), e.getValue());
		}
	}
}
