package de.benjaminborbe.search.service;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.search.api.SearchResult;

public class SearchServiceSearchResultComparatorUnitTest {

	@Test
	public void testCompare() {
		final SearchServiceSearchResultComparatorPrio b = new SearchServiceSearchResultComparatorPrio();
		final SearchServiceSearchResultComparatorName a = new SearchServiceSearchResultComparatorName();
		final SearchServiceSearchResultComparator comparator = new SearchServiceSearchResultComparator(a, b);
		assertTrue(comparator.compare(createComponent("name"), createComponent("name")) == 0);
		assertTrue(comparator.compare(createComponent("url"), createComponent("url")) == 0);
		assertTrue(comparator.compare(createComponent("URL"), createComponent("url")) == 0);
		assertTrue(comparator.compare(createComponent("GOOGLE"), createComponent("GOOGLE")) == 0);
		assertTrue(comparator.compare(createComponent("a"), createComponent("b")) < 0);
		assertTrue(comparator.compare(createComponent("b"), createComponent("a")) > 0);
		assertTrue(comparator.compare(createComponent("url"), createComponent("b")) > 0);
		assertTrue(comparator.compare(createComponent("b"), createComponent("url")) < 0);
		assertTrue(comparator.compare(createComponent("url"), createComponent("google")) > 0);
		assertTrue(comparator.compare(createComponent("google"), createComponent("url")) < 0);
	}

	private SearchResult createComponent(final String name) {
		final SearchResult component = EasyMock.createMock(SearchResult.class);
		EasyMock.expect(component.getType()).andReturn(name).anyTimes();
		EasyMock.replay(component);
		return component;
	}
}
