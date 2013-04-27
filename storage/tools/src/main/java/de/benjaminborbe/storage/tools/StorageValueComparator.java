package de.benjaminborbe.storage.tools;

import de.benjaminborbe.storage.api.StorageValue;
import de.benjaminborbe.tools.util.ComparatorBase;

import java.io.UnsupportedEncodingException;

public class StorageValueComparator extends ComparatorBase<StorageValue, String> {

	@Override
	public String getValue(final StorageValue o) {
		try {
			return o.getString();
		} catch (final UnsupportedEncodingException e) {
			return null;
		}
	}

}
