package de.benjaminborbe.lucene.index.util;

import java.io.IOException;

import org.apache.lucene.store.Directory;

public interface LuceneIndexFactory {

	Directory getLuceneIndex(final String indexName) throws IOException;
}
