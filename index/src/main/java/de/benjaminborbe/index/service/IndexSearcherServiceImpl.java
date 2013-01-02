package de.benjaminborbe.index.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
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

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.index.IndexConstants;
import de.benjaminborbe.index.api.IndexSearchResult;
import de.benjaminborbe.index.api.IndexSearcherService;
import de.benjaminborbe.index.util.IndexFactory;

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
	public List<IndexSearchResult> search(final String indexName, final String searchQuery, final int hitsPerPage) {
		logger.debug("search in index: " + indexName + " for " + searchQuery);
		final List<IndexSearchResult> result = new ArrayList<IndexSearchResult>();
		try {
			final Directory index = indexFactory.getIndex(indexName);

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

			final Analyzer analyzer = new StandardAnalyzer(IndexConstants.LUCENE_VERSION);
			final MultiFieldQueryParser queryParser = new MultiFieldQueryParser(IndexConstants.LUCENE_VERSION, buildFields(), analyzer);
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

		}
		catch (final IOException e) {
			logger.error(e.getClass().getName(), e);
		}
		catch (final ParseException e) {
			logger.error(e.getClass().getName(), e);
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
		final String url = d.get(IndexField.URL.getFieldName());
		final String title = d.get(IndexField.TITLE.getFieldName());
		final String content = d.get(IndexField.CONTENT.getFieldName());
		return new IndexSearchResultImpl(index, url, title, content);
	}
}
