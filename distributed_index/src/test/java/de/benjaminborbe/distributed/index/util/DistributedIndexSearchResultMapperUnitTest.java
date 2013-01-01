package de.benjaminborbe.distributed.index.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import de.benjaminborbe.distributed.index.api.DistributedIndexPageIdentifier;
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
			final String columnName = mapper.toString(new DistributedIndexSearchResultImpl(rating, new DistributedIndexPageIdentifier(pageId)));
			assertNotNull(columnName);
			final DistributedIndexSearchResult object = mapper.fromString(columnName);
			assertEquals(pageId, object.getId().getId());
			assertEquals(rating, object.getRating());
		}
		{
			final String pageId = "42";
			final Integer rating = Integer.MAX_VALUE;
			final String columnName = mapper.toString(new DistributedIndexSearchResultImpl(rating, new DistributedIndexPageIdentifier(pageId)));
			assertNotNull(columnName);
			final DistributedIndexSearchResult object = mapper.fromString(columnName);
			assertEquals(pageId, object.getId().getId());
			assertEquals(rating, object.getRating());
		}
		{
			final String pageId = "23";
			final Integer rating = 0;
			final String columnName = mapper.toString(new DistributedIndexSearchResultImpl(rating, new DistributedIndexPageIdentifier(pageId)));
			assertNotNull(columnName);
			final DistributedIndexSearchResult object = mapper.fromString(columnName);
			assertEquals(pageId, object.getId().getId());
			assertEquals(rating, object.getRating());
		}
	}

	@Test
	public void testOrder() throws Exception {
		final ParseUtil parseUtil = new ParseUtilImpl();
		final DistributedIndexSearchResultMapper mapper = new DistributedIndexSearchResultMapper(parseUtil);

		final List<String> columnNames = new ArrayList<String>();
		columnNames.add(mapper.toString(new DistributedIndexSearchResultImpl(0, new DistributedIndexPageIdentifier("a"))));
		columnNames.add(mapper.toString(new DistributedIndexSearchResultImpl(10, new DistributedIndexPageIdentifier("a"))));
		columnNames.add(mapper.toString(new DistributedIndexSearchResultImpl(200, new DistributedIndexPageIdentifier("a"))));
		columnNames.add(mapper.toString(new DistributedIndexSearchResultImpl(Integer.MAX_VALUE, new DistributedIndexPageIdentifier("a"))));

		Collections.sort(columnNames);

		assertEquals(new Integer(Integer.MAX_VALUE), mapper.fromString(columnNames.get(0)).getRating());
		assertEquals(new Integer(200), mapper.fromString(columnNames.get(1)).getRating());
		assertEquals(new Integer(10), mapper.fromString(columnNames.get(2)).getRating());
		assertEquals(new Integer(0), mapper.fromString(columnNames.get(3)).getRating());

	}
}
