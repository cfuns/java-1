package de.benjaminborbe.lucene.index.service;

import java.io.IOException;
import java.net.URL;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.lucene.index.LuceneIndexConstants;
import de.benjaminborbe.lucene.index.api.LuceneIndexerService;
import de.benjaminborbe.lucene.index.api.LuceneIndexerServiceException;
import de.benjaminborbe.lucene.index.util.LuceneIndexFactory;
import de.benjaminborbe.tools.util.StringUtil;

@Singleton
public class LuceneIndexerServiceImpl implements LuceneIndexerService {

	private final Logger logger;

	private final LuceneIndexFactory indexFactory;

	private final StringUtil stringUtil;

	@Inject
	public LuceneIndexerServiceImpl(final Logger logger, final LuceneIndexFactory indexFactory, final StringUtil stringUtil) {
		this.logger = logger;
		this.indexFactory = indexFactory;
		this.stringUtil = stringUtil;
	}

	@Override
	public void addToLuceneIndex(final String indexName, final URL url, final String title, final String content) throws LuceneIndexerServiceException {
		logger.info("add to index: " + indexName + " url: " + url.toExternalForm() + " title: " + title + " content: " + stringUtil.shorten(content, 100));

		if (!validateInput(indexName, url, title, content)) {
			logger.warn("input not valid, skipping add to index");
			return;
		}
		IndexWriter indexWriter = null;
		try {
			final Directory index = indexFactory.getLuceneIndex(indexName);
			final Analyzer analyzer = new StandardAnalyzer(LuceneIndexConstants.LUCENE_VERSION);
			final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneIndexConstants.LUCENE_VERSION, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// TODO only one indexwriter
			indexWriter = new IndexWriter(index, indexWriterConfig);

			final Term term = new Term(LuceneIndexField.ID.getFieldName(), url.toExternalForm());
			final Document doc = new Document();
			doc.add(new StringField(LuceneIndexField.ID.getFieldName(), url.toExternalForm(), Field.Store.YES));
			doc.add(new TextField(LuceneIndexField.URL.getFieldName(), url.toExternalForm(), Field.Store.YES));
			doc.add(new TextField(LuceneIndexField.TITLE.getFieldName(), title, Field.Store.YES));
			doc.add(new TextField(LuceneIndexField.CONTENT.getFieldName(), content, Field.Store.YES));
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
			throw new LuceneIndexerServiceException("IOException", e);
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
	public void clear(final String indexName) throws LuceneIndexerServiceException {
		IndexWriter indexWriter = null;
		try {

			final Directory index = indexFactory.getLuceneIndex(indexName);
			final Analyzer analyzer = new StandardAnalyzer(LuceneIndexConstants.LUCENE_VERSION);
			final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneIndexConstants.LUCENE_VERSION, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

			// TODO only one indexwriter
			indexWriter = new IndexWriter(index, indexWriterConfig);

			indexWriter.deleteAll();

			indexWriter.commit();
			indexWriter.close();
			indexWriter = null;
		}
		catch (final IOException e) {
			throw new LuceneIndexerServiceException(e);
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
