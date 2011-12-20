package de.benjaminborbe.storage.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.benjaminborbe.storage.api.PersistentStorageService;

public class PersistentStorageServiceMock extends StorageServiceMock implements PersistentStorageService {

	@Override
	public Collection<String> findByIdPrefix(final String columnFamily, final String idStartWith) {

		final Set<String> result = new HashSet<String>();
		final HashMap<String, HashMap<String, String>> cfData = storageData.get(columnFamily);
		if (cfData == null)
			return null;
		for (final String id : cfData.keySet()) {
			if (id.startsWith(idStartWith)) {
				result.add(id);
			}
		}
		return result;
	}

	@Override
	public List<String> list(final String columnFamily) {

		return null;
	}

}
