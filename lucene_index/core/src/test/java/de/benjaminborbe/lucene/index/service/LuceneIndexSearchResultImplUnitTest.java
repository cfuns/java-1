package de.benjaminborbe.lucene.index.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.benjaminborbe.lucene.index.api.LuceneIndexSearchResult;

public class LuceneIndexSearchResultImplUnitTest {

	@Test
	public void testEquals() throws Exception {
		final LuceneIndexSearchResult a = new LuceneIndexSearchResultImpl(null, "http://example.com/a", null, null);
		final LuceneIndexSearchResult b = new LuceneIndexSearchResultImpl(null, "http://example.com/b", null, null);
		final LuceneIndexSearchResult c = new LuceneIndexSearchResultImpl(null, "http://example.com/b", null, null);
		assertThat(a.equals(b), is(false));
		assertThat(b.equals(c), is(true));
	}
}
