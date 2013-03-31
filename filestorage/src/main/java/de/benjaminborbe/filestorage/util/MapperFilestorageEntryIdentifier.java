package de.benjaminborbe.filestorage.util;

import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;
import de.benjaminborbe.tools.mapper.Mapper;

public class MapperFilestorageEntryIdentifier implements Mapper<FilestorageEntryIdentifier> {

	@Override
	public String toString(final FilestorageEntryIdentifier value) {
		return value != null ? value.getId() : null;
	}

	@Override
	public FilestorageEntryIdentifier fromString(final String value) {
		return value != null ? new FilestorageEntryIdentifier(value) : null;
	}

}
