package de.benjaminborbe.filestorage.dao;

import de.benjaminborbe.api.IdentifierBuilder;
import de.benjaminborbe.filestorage.api.FilestorageEntryIdentifier;

public class FilestorageEntryIdentifierBuilder implements IdentifierBuilder<String, FilestorageEntryIdentifier> {

	@Override
	public FilestorageEntryIdentifier buildIdentifier(final String value) {
		return new FilestorageEntryIdentifier(value);
	}

}
