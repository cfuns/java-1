package de.benjaminborbe.index.service;

import java.io.IOException;
import java.net.URL;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.util.IndexFactory;

@Singleton
public class IndexerServiceImpl implements IndexerService {

	private final Logger logger;

	private final IndexFactory indexFactory;

	@Inject
	public IndexerServiceImpl(final Logger logger, final IndexFactory indexFactory) {
		this.logger = logger;
		this.indexFactory = indexFactory;
	}

	@Override
	public void addToIndex(final String indexName, final URL url, final String title, final String content) {
		logger.debug("add to index: " + indexName + " url: " + url.toExternalForm() + " title: " + title + " content: "
				+ content);

		try {
			final Directory index = indexFactory.getIndex(indexName);
			final StandardAnalyzer analyzer = new StandardAnalyzer();
			final IndexWriter indexWriter = new IndexWriter(index, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);

			final Document doc = new Document();
			doc.add(new Field(IndexField.URL.getFieldName(), url.toExternalForm(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field(IndexField.TITLE.getFieldName(), title, Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field(IndexField.CONTENT.getFieldName(), content, Field.Store.YES, Field.Index.ANALYZED));
			indexWriter.addDocument(doc);
			indexWriter.commit();

			indexWriter.close();
		}
		catch (final IOException e) {
			logger.error("IOException", e);
		}
	}
}
