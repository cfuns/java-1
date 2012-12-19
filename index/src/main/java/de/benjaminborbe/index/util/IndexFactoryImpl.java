package de.benjaminborbe.index.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.config.IndexConfig;

@Singleton
public class IndexFactoryImpl implements IndexFactory {

	private final Logger logger;

	private final Map<String, FSDirectory> indexes = new HashMap<String, FSDirectory>();

	private final IndexConfig indexConfig;

	@Inject
	public IndexFactoryImpl(final Logger logger, final IndexConfig indexConfig) {
		this.logger = logger;
		this.indexConfig = indexConfig;
	}

	@Override
	public synchronized FSDirectory getIndex(final String indexName) throws IOException {
		logger.trace("get Index for " + indexName);
		if (!indexes.containsKey(indexName)) {
			logger.trace("create new index for " + indexName);
			final FSDirectory fs = MMapDirectory.open(getIndexDirectory(indexName));
			indexes.put(indexName, fs);
		}
		return indexes.get(indexName);
	}

	protected File getIndexDirectory(final String indexName) throws IOException {
		final String dirName = indexConfig.getIndexDirectory() + "/lucene_index_" + indexName;
		logger.info("getIndexDirectory => " + dirName);
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
