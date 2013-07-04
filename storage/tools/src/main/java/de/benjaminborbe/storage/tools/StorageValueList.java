package de.benjaminborbe.storage.tools;

import de.benjaminborbe.storage.api.StorageValue;

import java.util.ArrayList;
import java.util.List;

public class StorageValueList extends ArrayList<StorageValue> implements List<StorageValue> {

	private static final long serialVersionUID = -2781013816249947588L;

	private final String encoding;

	public StorageValueList(final String encoding) {
		this.encoding = encoding;
	}

	public StorageValueList add(final String value) {
		add(new StorageValue(value, encoding));
		return this;
	}
}
