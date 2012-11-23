package de.benjaminborbe.index.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.util.IndexFactory;

import de.benjaminborbe.index.*;

@Singleton
public class IndexSearcherServiceImpl implements IndexSearcherService {

	private final Logger logger;

	private final IndexFactory indexFactory;

	@Inject
	public IndexSearcherServiceImpl(final Logger logger, final IndexFactory indexFactory) {
		this.logger = logger;
		this.indexFactory = indexFactory;
	}

	@Override
	public List<IndexSearchResult> search(final String indexName, final String searchQuery) {
		logger.trace("search in index: " + indexName + " for " + searchQuery);
		final List<IndexSearchResult> result = new ArrayList<IndexSearchResult>();
		try {
			final Directory index = indexFactory.getIndex(indexName);
			final Version version = IndexConstants.LUCENE_VERSION;
			final StandardAnalyzer analyzer = new StandardAnalyzer(version);
			// parse query over multiple fields
			final Query q = new MultiFieldQueryParser(IndexConstants.LUCENE_VERSION, buildFields(), analyzer).parse(searchQuery);

			// searching ...
			final int hitsPerPage = 10;
			final boolean readOnly = true;
			final IndexReader indexReader = IndexReader.open(index, readOnly);
			final IndexSearcher searcher = new IndexSearcher(indexReader);
			final boolean docsScoredInOrder = true;
			final TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, docsScoredInOrder);
			searcher.search(q, collector);
			final ScoreDoc[] hits = collector.topDocs().scoreDocs;

			// output results
			logger.debug("Found " + hits.length + " hits.");
			for (int i = 0; i < hits.length; ++i) {
				final int docId = hits[i].doc;
				final Document document = searcher.doc(docId);
				result.add(buildSearchResult(indexName, document));
			}

			searcher.close();
		}
		catch (final IOException e) {
			logger.error("IOException", e);
		}
		catch (final ParseException e) {
			logger.error("ParseException", e);
		}
		return result;
	}

	private String[] buildFields() {
		final List<String> fields = new ArrayList<String>();
		for (final IndexField field : IndexField.values()) {
			fields.add(field.getFieldName());
		}
		return fields.toArray(new String[fields.size()]);
	}

	private IndexSearchResult buildSearchResult(final String index, final Document d) throws MalformedURLException {
		final URL url = new URL(d.get(IndexField.URL.getFieldName()));
		final String title = d.get(IndexField.TITLE.getFieldName());
		final String content = d.get(IndexField.CONTENT.getFieldName());
		return new IndexSearchResultImpl(index, url, title, content);
	}
}
