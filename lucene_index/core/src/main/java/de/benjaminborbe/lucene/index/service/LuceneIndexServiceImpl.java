package de.benjaminborbe.lucene.index.service;

import de.benjaminborbe.lucene.index.LuceneIndexConstants;
import de.benjaminborbe.lucene.index.api.LuceneIndexSearchResult;
import de.benjaminborbe.lucene.index.api.LuceneIndexService;
import de.benjaminborbe.lucene.index.api.LuceneIndexServiceException;
import de.benjaminborbe.lucene.index.util.LuceneIndexFactory;
import de.benjaminborbe.tools.util.StringUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class LuceneIndexServiceImpl implements LuceneIndexService {

	private final Logger logger;

	private final LuceneIndexFactory indexFactory;

	private final StringUtil stringUtil;

	@Inject
	public LuceneIndexServiceImpl(final Logger logger, final LuceneIndexFactory indexFactory, final StringUtil stringUtil) {
		this.logger = logger;
		this.indexFactory = indexFactory;
		this.stringUtil = stringUtil;
	}

	@Override
	public List<LuceneIndexSearchResult> search(final String indexName, final String searchQuery, final int hitsPerPage) {
		logger.debug("search in index: " + indexName + " for " + searchQuery);
		final List<LuceneIndexSearchResult> result = new ArrayList<>();
		try {
			final Directory index = indexFactory.getLuceneIndex(indexName);

			{
				final String[] words = searchQuery.split(" ");

				final BooleanQuery query = new BooleanQuery();
				for (final String field : buildFields()) {
					for (final String word : words) {
						final Query subquery = new TermQuery(new Term(field, word));
						query.add(subquery, BooleanClause.Occur.SHOULD);
					}
				}
			}

			final Analyzer analyzer = new StandardAnalyzer(LuceneIndexConstants.LUCENE_VERSION);
			final MultiFieldQueryParser queryParser = new MultiFieldQueryParser(LuceneIndexConstants.LUCENE_VERSION, buildFields(), analyzer);
			queryParser.setDefaultOperator(Operator.OR);
			final Query query = queryParser.parse(searchQuery);

			// searching...
			final IndexReader indexReader = DirectoryReader.open(index);
			final IndexSearcher searcher = new IndexSearcher(indexReader);
			final boolean docsScoredInOrder = true;
			final TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, docsScoredInOrder);
			searcher.search(query, collector);
			final ScoreDoc[] hits = collector.topDocs().scoreDocs;

			// output results
			logger.debug("Found " + hits.length + " hits.");
			for (int i = 0; i < hits.length; ++i) {
				final int docId = hits[i].doc;
				final Document document = searcher.doc(docId);
				result.add(buildSearchResult(indexName, document));
			}

		} catch (final IOException | ParseException e) {
			logger.error(e.getClass().getName(), e);
		}
		return result;
	}

	private String[] buildFields() {
		final List<String> fields = new ArrayList<>();
		for (final LuceneIndexField field : LuceneIndexField.values()) {
			fields.add(field.getFieldName());
		}
		return fields.toArray(new String[fields.size()]);
	}

	private LuceneIndexSearchResult buildSearchResult(final String index, final Document d) throws MalformedURLException {
		final String url = d.get(LuceneIndexField.URL.getFieldName());
		final String title = d.get(LuceneIndexField.TITLE.getFieldName());
		final String content = d.get(LuceneIndexField.CONTENT.getFieldName());
		return new LuceneIndexSearchResultImpl(index, url, title, content);
	}

	@Override
	public void addToIndex(final String indexName, final URL url, final String title, final String content) throws LuceneIndexServiceException {
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

			indexWriter = new IndexWriter(index, indexWriterConfig);

			final Term term = new Term(LuceneIndexField.ID.getFieldName(), url.toExternalForm());
			final Document doc = new Document();
			doc.add(new StringField(LuceneIndexField.ID.getFieldName(), url.toExternalForm(), Field.Store.YES));
			doc.add(new TextField(LuceneIndexField.URL.getFieldName(), url.toExternalForm(), Field.Store.YES));
			doc.add(new TextField(LuceneIndexField.TITLE.getFieldName(), title, Field.Store.YES));
			doc.add(new TextField(LuceneIndexField.CONTENT.getFieldName(), content, Field.Store.YES));
			indexWriter.updateDocument(term, doc, analyzer);

			indexWriter.forceMergeDeletes();
			indexWriter.commit();
			indexWriter.close();
			indexWriter = null;
		} catch (final IOException e) {
			logger.error("IOException", e);
			throw new LuceneIndexServiceException("IOException", e);
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (final IOException e) {
				}
			}
		}
	}

	@Override
	public void clear(final String indexName) throws LuceneIndexServiceException {
		IndexWriter indexWriter = null;
		try {

			final Directory index = indexFactory.getLuceneIndex(indexName);
			final Analyzer analyzer = new StandardAnalyzer(LuceneIndexConstants.LUCENE_VERSION);
			final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneIndexConstants.LUCENE_VERSION, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

			indexWriter = new IndexWriter(index, indexWriterConfig);

			indexWriter.deleteAll();

			indexWriter.commit();
			indexWriter.close();
			indexWriter = null;
		} catch (final IOException e) {
			throw new LuceneIndexServiceException(e);
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (final IOException e) {
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

	@Override
	public void removeFromIndex(final String indexName, final URL url) throws LuceneIndexServiceException {

		logger.info("remove form index: " + indexName + " url: " + url.toExternalForm());

		IndexWriter indexWriter = null;
		try {
			final Directory index = indexFactory.getLuceneIndex(indexName);
			final Analyzer analyzer = new StandardAnalyzer(LuceneIndexConstants.LUCENE_VERSION);
			final IndexWriterConfig indexWriterConfig = new IndexWriterConfig(LuceneIndexConstants.LUCENE_VERSION, analyzer);
			indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

			indexWriter = new IndexWriter(index, indexWriterConfig);

			final Term term = new Term(LuceneIndexField.ID.getFieldName(), url.toExternalForm());
			indexWriter.deleteDocuments(term);

			indexWriter.forceMergeDeletes();
			indexWriter.commit();
			indexWriter.close();
			indexWriter = null;
		} catch (final IOException e) {
			logger.error("IOException", e);
			throw new LuceneIndexServiceException("IOException", e);
		} finally {
			if (indexWriter != null) {
				try {
					indexWriter.close();
				} catch (final IOException e) {
				}
			}
		}

	}

}
