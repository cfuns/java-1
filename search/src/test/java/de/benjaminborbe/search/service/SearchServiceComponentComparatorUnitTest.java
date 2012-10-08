package de.benjaminborbe.search.service;

import static org.junit.Assert.*;

import org.easymock.EasyMock;
import org.junit.Test;

import de.benjaminborbe.search.api.SearchServiceComponent;

public class SearchServiceComponentComparatorUnitTest {

	@Test
	public void testCompare() {
		final SearchServiceComponentComparator comparator = new SearchServiceComponentComparator();
		assertEquals(0, comparator.compare(createComponent("name"), createComponent("name")));
		assertEquals(0, comparator.compare(createComponent("url"), createComponent("url")));
		assertEquals(0, comparator.compare(createComponent("URL"), createComponent("url")));
		assertEquals(0, comparator.compare(createComponent("GOOGLE"), createComponent("GOOGLE")));
		assertEquals(-1, comparator.compare(createComponent("a"), createComponent("b")));
		assertEquals(1, comparator.compare(createComponent("b"), createComponent("a")));
		assertEquals(-1, comparator.compare(createComponent("url"), createComponent("b")));
		assertEquals(1, comparator.compare(createComponent("b"), createComponent("url")));
		assertEquals(-1, comparator.compare(createComponent("url"), createComponent("google")));
		assertEquals(1, comparator.compare(createComponent("google"), createComponent("url")));
	}

	private SearchServiceComponent createComponent(final String name) {
		final SearchServiceComponent component = EasyMock.createMock(SearchServiceComponent.class);
		EasyMock.expect(component.getName()).andReturn(name).anyTimes();
		EasyMock.replay(component);
		return component;
	}
}
