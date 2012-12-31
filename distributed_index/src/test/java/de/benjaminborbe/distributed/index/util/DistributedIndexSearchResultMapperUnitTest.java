package de.benjaminborbe.distributed.index.util;

import static org.junit.Assert.*;

import org.junit.Test;

import de.benjaminborbe.distributed.index.api.DistributedIndexIdentifier;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.tools.util.ParseUtil;
import de.benjaminborbe.tools.util.ParseUtilImpl;

public class DistributedIndexSearchResultMapperUnitTest {

	@Test
	public void testBuildColumnName() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final DistributedIndexSearchResultMapper mapper = new DistributedIndexSearchResultMapper(parseUtil);

		{
			final String pageId = "1337";
			final Integer rating = 5;
			final String columnName = mapper.toString(new DistributedIndexSearchResultImpl(rating, new DistributedIndexIdentifier(pageId)));
			assertNotNull(columnName);
			final DistributedIndexSearchResult object = mapper.fromString(columnName);
			assertEquals(pageId, object.getId().getId());
			assertEquals(rating, object.getRating());
		}
		{
			final String pageId = "42";
			final Integer rating = Integer.MAX_VALUE;
			final String columnName = mapper.toString(new DistributedIndexSearchResultImpl(rating, new DistributedIndexIdentifier(pageId)));
			assertNotNull(columnName);
			final DistributedIndexSearchResult object = mapper.fromString(columnName);
			assertEquals(pageId, object.getId().getId());
			assertEquals(rating, object.getRating());
		}
		{
			final String pageId = "23";
			final Integer rating = 0;
			final String columnName = mapper.toString(new DistributedIndexSearchResultImpl(rating, new DistributedIndexIdentifier(pageId)));
			assertNotNull(columnName);
			final DistributedIndexSearchResult object = mapper.fromString(columnName);
			assertEquals(pageId, object.getId().getId());
			assertEquals(rating, object.getRating());
		}
	}
}
