package de.benjaminborbe.distributed.search.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.easymock.EasyMock;
import org.junit.Test;

public class NoStopWordPredicateUnitTest {

	@Test
	public void testStopWord() throws Exception {
		final StopWords stopWords = EasyMock.createMock(StopWords.class);
		EasyMock.expect(stopWords.contains("foo")).andReturn(true);
		EasyMock.replay(stopWords);
		final NoStopWordPredicate p = new NoStopWordPredicate(stopWords);
		assertThat(p.apply("foo"), is(false));
	}

	@Test
	public void testNoStopWord() throws Exception {
		final StopWords stopWords = EasyMock.createMock(StopWords.class);
		EasyMock.expect(stopWords.contains("foo")).andReturn(false);
		EasyMock.replay(stopWords);
		final NoStopWordPredicate p = new NoStopWordPredicate(stopWords);
		assertThat(p.apply("foo"), is(true));
	}

}
