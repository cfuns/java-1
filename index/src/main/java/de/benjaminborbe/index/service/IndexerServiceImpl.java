package de.benjaminborbe.index.service;

import java.io.IOException;
import java.net.URL;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.util.IndexFactory;
import de.benjaminborbe.tools.util.StringUtil;

@Singleton
public class IndexerServiceImpl implements IndexerService {

	private final Logger logger;

	private final IndexFactory indexFactory;

	private final StringUtil stringUtil;

	@Inject
	public IndexerServiceImpl(final Logger logger, final IndexFactory indexFactory, final StringUtil stringUtil) {
		this.logger = logger;
		this.indexFactory = indexFactory;
		this.stringUtil = stringUtil;
	}

	@Override
	public void addToIndex(final String indexName, final URL url, final String title, final String content) {
		logger.debug("add to index: " + indexName + " url: " + url.toExternalForm() + " title: " + title + " content: " + stringUtil.shorten(content, 20));

		try {
			final Directory index = indexFactory.getIndex(indexName);
			final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
			final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			final IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig);

			final Term term = new Term(IndexField.ID.getFieldName(), url.toExternalForm());
			final Document doc = new Document();
			doc.add(new Field(IndexField.URL.getFieldName(), url.toExternalForm(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field(IndexField.TITLE.getFieldName(), title, Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field(IndexField.CONTENT.getFieldName(), content, Field.Store.YES, Field.Index.ANALYZED));
			indexWriter.updateDocument(term, doc, analyzer);

			// indexWriter.deleteDocuments(term);
			// indexWriter.addDocument(doc);

			indexWriter.commit();
			indexWriter.close();
		}
		catch (final IOException e) {
			logger.error("IOException", e);
		}
	}

	@Override
	public void clear(final String indexName) {

		try {
			final Directory index = indexFactory.getIndex(indexName);
			final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
			final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_35, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			final IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig);

			indexWriter.deleteAll();

			indexWriter.commit();
			indexWriter.close();
		}
		catch (final IOException e) {
			logger.error("IOException", e);
		}

	}
}
