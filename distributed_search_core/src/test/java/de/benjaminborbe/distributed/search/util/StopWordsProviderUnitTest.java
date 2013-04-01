package de.benjaminborbe.distributed.search.util;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.easymock.EasyMock;
import org.junit.Test;
import org.slf4j.Logger;

import de.benjaminborbe.tools.guice.ProviderAdapter;

public class StopWordsProviderUnitTest {

	@Test
	public void testReadStopWords() throws Exception {
		final Logger logger = EasyMock.createNiceMock(Logger.class);
		EasyMock.replay(logger);

		final StopWordsImpl stopWordsImpl = new StopWordsImpl(logger);
		final StopWordsProvider provider = new StopWordsProvider(new ProviderAdapter<StopWordsImpl>(stopWordsImpl));
		final StopWords stopWords = provider.get();
		assertThat(stopWords.contains("about"), is(true));
		assertThat(stopWords.contains("foobar"), is(false));
	}
}
