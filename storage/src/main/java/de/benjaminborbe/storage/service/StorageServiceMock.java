package de.benjaminborbe.storage.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.storage.api.StorageService;

@Singleton
public class StorageServiceMock implements StorageService {

	protected final HashMap<String, HashMap<String, HashMap<String, String>>> storageData = new HashMap<String, HashMap<String, HashMap<String, String>>>();

	@Inject
	public StorageServiceMock() {
	}

	@Override
	public String get(final String columnFamily, final String id, final String key) {
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return null;
		final HashMap<String, String> idData = cfData.get(id);
		if (idData == null)
			return null;
		return idData.get(key);
	}

	@Override
	public void delete(final String columnFamily, final String id, final String key) {
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return;
		final HashMap<String, String> idData = cfData.get(id);
		if (idData == null)
			return;
		idData.remove(key);
	}

	@Override
	public void set(final String columnFamily, final String id, final String key, final String value) {
		HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null) {
			cfData = new HashMap<String, HashMap<String, String>>();
			storageData.put(columnFamily, cfData);
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
		HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null) {
			cfData = new HashMap<String, HashMap<String, String>>();
			storageData.put(columnFamily, cfData);
		}
		final HashMap<String, String> idData = new HashMap<String, String>();
		cfData.put(id, idData);
		for (final Entry<String, String> e : data.entrySet()) {
			idData.put(e.getKey(), e.getValue());
		}
	}

}
