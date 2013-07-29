package de.benjaminborbe.search.core.service;

import de.benjaminborbe.search.api.SearchResult;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchServiceSearchResultComparatorUnitTest {

	@Test
	public void testCompare() {
		final SearchServiceSearchResultComparatorPrio b = new SearchServiceSearchResultComparatorPrio();
		final SearchServiceSearchResultComparatorName a = new SearchServiceSearchResultComparatorName();
		final SearchServiceSearchResultComparatorMatches c = new SearchServiceSearchResultComparatorMatches();
		final SearchServiceSearchResultComparator comparator = new SearchServiceSearchResultComparator(a, b, c);
		assertTrue(comparator.compare(createComponent("name"), createComponent("name")) == 0);
		assertTrue(comparator.compare(createComponent("url"), createComponent("url")) == 0);
		assertTrue(comparator.compare(createComponent("URL"), createComponent("url")) == 0);
		assertTrue(comparator.compare(createComponent("GOOGLE"), createComponent("GOOGLE")) == 0);
		assertTrue(comparator.compare(createComponent("a"), createComponent("b")) < 0);
		assertTrue(comparator.compare(createComponent("b"), createComponent("a")) > 0);
		assertTrue(comparator.compare(createComponent("url"), createComponent("b")) < 0);
		assertTrue(comparator.compare(createComponent("b"), createComponent("url")) > 0);
		assertTrue(comparator.compare(createComponent("url"), createComponent("google")) < 0);
		assertTrue(comparator.compare(createComponent("google"), createComponent("url")) > 0);
	}

	private SearchResult createComponent(final String type, final int matchCounter) {
		final SearchResult component = EasyMock.createMock(SearchResult.class);
		EasyMock.expect(component.getType()).andReturn(type).anyTimes();
		EasyMock.expect(component.getMatchCounter()).andReturn(matchCounter).anyTimes();
		EasyMock.replay(component);
		return component;
	}

	private SearchResult createComponent(final String type) {
		return createComponent(type, 1);
	}

	@Test
	public void testCompareList() {

		final SearchServiceSearchResultComparatorPrio b = new SearchServiceSearchResultComparatorPrio();
		final SearchServiceSearchResultComparatorName a = new SearchServiceSearchResultComparatorName();
		final SearchServiceSearchResultComparatorMatches c = new SearchServiceSearchResultComparatorMatches();
		final SearchServiceSearchResultComparator comparator = new SearchServiceSearchResultComparator(a, b, c);

		final List<SearchResult> list = new ArrayList<SearchResult>();
		list.add(createComponent("URL"));
		list.add(createComponent("GOOGLE"));
		list.add(createComponent("CONFLUENCE"));
		list.add(createComponent("BOOKMARK"));
		list.add(createComponent("WEB"));
		list.add(createComponent("bla"));
		list.add(createComponent(null));

		Collections.sort(list, comparator);
		assertEquals("URL", list.get(0).getType());
		assertEquals("GOOGLE", list.get(1).getType());
		assertEquals("CONFLUENCE", list.get(2).getType());
		assertEquals("BOOKMARK", list.get(3).getType());
		assertEquals("WEB", list.get(4).getType());
		assertEquals("bla", list.get(5).getType());
		assertEquals(null, list.get(6).getType());
	}

	@Test
	public void testPrio() {
		final SearchServiceSearchResultComparatorPrio comparator = new SearchServiceSearchResultComparatorPrio();

		final List<SearchResult> list = new ArrayList<SearchResult>();
		list.add(createComponent("URL"));
		list.add(createComponent("GOOGLE"));
		list.add(createComponent("CONFLUENCE"));
		list.add(createComponent("BOOKMARK"));
		list.add(createComponent("WEB"));
		list.add(createComponent("bla"));
		list.add(createComponent(null));

		Collections.sort(list, comparator);
		assertEquals("URL", list.get(0).getType());
		assertEquals("GOOGLE", list.get(1).getType());
		assertEquals("CONFLUENCE", list.get(2).getType());
		assertEquals("BOOKMARK", list.get(3).getType());
		assertEquals("WEB", list.get(4).getType());
		assertEquals("bla", list.get(5).getType());
		assertEquals(null, list.get(6).getType());
	}

	@Test
	public void testName() {
		final SearchServiceSearchResultComparatorName comparator = new SearchServiceSearchResultComparatorName();

		final List<SearchResult> list = new ArrayList<SearchResult>();
		list.add(createComponent("URL"));
		list.add(createComponent("GOOGLE"));
		list.add(createComponent("CONFLUENCE"));
		list.add(createComponent("BOOKMARK"));
		list.add(createComponent("WEB"));
		list.add(createComponent("bla"));
		list.add(createComponent(null));

		Collections.sort(list, comparator);
		assertEquals("bla", list.get(0).getType());
		assertEquals("BOOKMARK", list.get(1).getType());
		assertEquals("CONFLUENCE", list.get(2).getType());
		assertEquals("GOOGLE", list.get(3).getType());
		assertEquals("URL", list.get(4).getType());
		assertEquals("WEB", list.get(5).getType());
		assertEquals(null, list.get(6).getType());
	}

	@Test
	public void testCompareMatchCounter() {

		final SearchServiceSearchResultComparatorPrio b = new SearchServiceSearchResultComparatorPrio();
		final SearchServiceSearchResultComparatorName a = new SearchServiceSearchResultComparatorName();
		final SearchServiceSearchResultComparatorMatches c = new SearchServiceSearchResultComparatorMatches();
		final SearchServiceSearchResultComparator comparator = new SearchServiceSearchResultComparator(a, b, c);

		final List<SearchResult> list = new ArrayList<SearchResult>();
		list.add(createComponent("URL", 1));
		list.add(createComponent("GOOGLE", 2));
		list.add(createComponent("CONFLUENCE", 3));

		Collections.sort(list, comparator);
		assertEquals("CONFLUENCE", list.get(0).getType());
		assertEquals("GOOGLE", list.get(1).getType());
		assertEquals("URL", list.get(2).getType());
	}
}
