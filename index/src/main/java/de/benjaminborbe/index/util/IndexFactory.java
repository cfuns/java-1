package de.benjaminborbe.index.util;

import java.io.IOException;

import org.apache.lucene.store.Directory;

public interface IndexFactory {

	Directory getIndex(final String indexName) throws IOException;
}
