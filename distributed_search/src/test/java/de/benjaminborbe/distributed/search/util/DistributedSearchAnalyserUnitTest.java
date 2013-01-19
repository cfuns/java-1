package de.benjaminborbe.distributed.search.util;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.benjaminborbe.tools.search.SearchUtil;

public class DistributedSearchAnalyserUnitTest {

	@Test
	public void testParse() throws Exception {
		final SearchUtil searchUtil = new SearchUtil();
		final DistributedSearchAnalyser analyser = new DistributedSearchAnalyser(searchUtil);
		assertThat(analyser.parseSearchTerm("foo").size(), is(1));
		assertThat(analyser.parseSearchTerm("foo"), is(hasItem("foo")));
		assertThat(analyser.parseSearchTerm("").size(), is(0));
		assertThat(analyser.parseSearchTerm(null).size(), is(0));
	}

	@Test
	public void testUmlaut() {
		final SearchUtil searchUtil = new SearchUtil();
		final DistributedSearchAnalyser analyser = new DistributedSearchAnalyser(searchUtil);
		assertThat(analyser.parseSearchTerm("aüb"), is(hasItem("aüb")));
		assertThat(analyser.parseSearchTerm("aäb"), is(hasItem("aäb")));
		assertThat(analyser.parseSearchTerm("aöb"), is(hasItem("aöb")));

		assertThat(analyser.parseSearchTerm("aÜb"), is(hasItem("aüb")));
		assertThat(analyser.parseSearchTerm("aÄb"), is(hasItem("aäb")));
		assertThat(analyser.parseSearchTerm("aÖb"), is(hasItem("aöb")));

		assertThat(analyser.parseSearchTerm("aßb"), is(hasItem("aßb")));
	}

	@Test
	public void testparseWordRatingCount() {
		final SearchUtil searchUtil = new SearchUtil();
		final DistributedSearchAnalyser analyser = new DistributedSearchAnalyser(searchUtil);
		assertThat(analyser.parseWordRating("foo").get("foo"), is(1));
		assertThat(analyser.parseWordRating("foo foo").get("foo"), is(2));
		assertThat(analyser.parseWordRating("foo foo foo").get("foo"), is(3));
	}
}
