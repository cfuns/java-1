package de.benjaminborbe.index.service;

import java.io.IOException;
import java.net.URL;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.IndexConstants;
import de.benjaminborbe.index.api.IndexerService;
import de.benjaminborbe.index.api.IndexerServiceException;
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
	public void addToIndex(final String indexName, final URL url, final String title, final String content) throws IndexerServiceException {
		logger.info("add to index: " + indexName + " url: " + url.toExternalForm() + " title: " + title + " content: " + stringUtil.shorten(content, 100));

		if (!validateInput(indexName, url, title, content)) {
			logger.warn("input not valid, skipping add to index");
			return;
		}
		IndexWriter indexWriter = null;
		try {
			final Directory index = indexFactory.getIndex(indexName);
			final Analyzer analyzer = new StandardAnalyzer(IndexConstants.LUCENE_VERSION);
			final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(IndexConstants.LUCENE_VERSION, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// TODO only one indexwriter
			indexWriter = new IndexWriter(index, indexWriterConfig);

			final Term term = new Term(IndexField.ID.getFieldName(), url.toExternalForm());
			final Document doc = new Document();
			doc.add(new StringField(IndexField.ID.getFieldName(), url.toExternalForm(), Field.Store.YES));
			doc.add(new StringField(IndexField.URL.getFieldName(), url.toExternalForm(), Field.Store.YES));
			doc.add(new StringField(IndexField.TITLE.getFieldName(), title, Field.Store.YES));
			doc.add(new StringField(IndexField.CONTENT.getFieldName(), content, Field.Store.YES));
			indexWriter.updateDocument(term, doc, analyzer);

			// indexWriter.deleteDocuments(term);
			// indexWriter.addDocument(doc);
			indexWriter.forceMergeDeletes();
			indexWriter.commit();
			indexWriter.close();
			indexWriter = null;
		}
		catch (final IOException e) {
			logger.error("IOException", e);
			throw new IndexerServiceException("IOException", e);
		}
		finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				}
				catch (final CorruptIndexException e) {
				}
				catch (final IOException e) {
				}
			}
		}
	}

	@Override
	public void clear(final String indexName) throws IndexerServiceException {
		IndexWriter indexWriter = null;
		try {
			final Directory index = indexFactory.getIndex(indexName);
			final Analyzer analyzer = new StandardAnalyzer(IndexConstants.LUCENE_VERSION);
			final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(IndexConstants.LUCENE_VERSION, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// TODO only one indexwriter
			indexWriter = new IndexWriter(index, indexWriterConfig);

			indexWriter.deleteAll();

			indexWriter.commit();
			indexWriter.close();
			indexWriter = null;
		}
		catch (final IOException e) {
			throw new IndexerServiceException(e);
		}
		finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				}
				catch (final CorruptIndexException e) {
				}
				catch (final IOException e) {
				}
			}
		}
	}

	protected boolean validateInput(final String indexName, final URL url, final String title, final String content) {
		if (indexName == null || indexName.length() < 1) {
			logger.warn("indexName not valid: " + indexName);
			return false;
		}
		if (url == null) {
			logger.warn("url not valid: " + url);
			return false;
		}
		if (title == null || title.length() < 1) {
			logger.warn("title not valid: " + title);
			return false;
		}
		if (content == null || content.length() < 1) {
			logger.warn("content not valid: " + content);
			return false;
		}
		logger.trace("input valid");
		return true;
	}
}
