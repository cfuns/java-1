package de.benjaminborbe.lucene.index.util;

import org.apache.lucene.store.Directory;

import java.io.IOException;

public interface LuceneIndexFactory {

	Directory getLuceneIndex(final String indexName) throws IOException;
}
