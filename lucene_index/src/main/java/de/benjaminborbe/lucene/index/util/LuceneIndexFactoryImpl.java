package de.benjaminborbe.lucene.index.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.lucene.index.config.LuceneIndexConfig;

@Singleton
public class LuceneIndexFactoryImpl implements LuceneIndexFactory {

	private final Logger logger;

	private final Map<String, FSDirectory> indexes = new HashMap<String, FSDirectory>();

	private final LuceneIndexConfig indexConfig;

	@Inject
	public LuceneIndexFactoryImpl(final Logger logger, final LuceneIndexConfig indexConfig) {
		this.logger = logger;
		this.indexConfig = indexConfig;
	}

	@Override
	public synchronized FSDirectory getLuceneIndex(final String indexName) throws IOException {
		logger.trace("get LuceneIndex for " + indexName);
		if (!indexes.containsKey(indexName)) {
			logger.trace("create new index for " + indexName);
			final FSDirectory fs = MMapDirectory.open(getLuceneIndexDirectory(indexName));
			indexes.put(indexName, fs);
		}
		return indexes.get(indexName);
	}

	protected File getLuceneIndexDirectory(final String indexName) throws IOException {
		final String dirName = indexConfig.getLuceneIndexDirectory() + "/lucene_index_" + indexName;
		logger.info("getLuceneIndexDirectory => " + dirName);
		final File dir = new File(dirName);
		if (dir.exists()) {
			return dir;
		}
		else if (dir.mkdirs()) {
			return dir;
		}
		else {
			throw new IOException("can't create index direcory " + dirName);
		}
	}
}
