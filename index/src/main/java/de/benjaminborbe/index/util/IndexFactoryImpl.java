package de.benjaminborbe.index.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IndexFactoryImpl implements IndexFactory {

	private final Logger logger;

	private final Map<String, FSDirectory> indexes = new HashMap<String, FSDirectory>();

	@Inject
	public IndexFactoryImpl(final Logger logger) {
		this.logger = logger;
	}

	@Override
	public synchronized FSDirectory getIndex(final String indexName) throws IOException {
		logger.trace("get Index for " + indexName);
		if (!indexes.containsKey(indexName)) {
			logger.trace("create new index for " + indexName);
			final FSDirectory fs = FSDirectory.open(getIndexDirectory(indexName));
			indexes.put(indexName, fs);
		}
		return indexes.get(indexName);
	}

	protected File getTempDir() {
		final String property = "java.io.tmpdir";
		final File tmpDir = new File(System.getProperty(property));
		if (tmpDir.isDirectory() && tmpDir.canWrite()) {
			return tmpDir;
		}
		else {
			return new File("/tmp");
		}
	}

	protected File getIndexDirectory(final String indexName) throws IOException {
		final String dirName = getTempDir().getAbsolutePath() + "/lucene_index_" + indexName;
		logger.trace("getIndexDirectory => " + dirName);
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
