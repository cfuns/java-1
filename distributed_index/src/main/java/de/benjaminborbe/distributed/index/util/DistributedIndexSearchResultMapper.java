package de.benjaminborbe.distributed.index.util;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.index.api.DistributedIndexPageIdentifier;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class DistributedIndexSearchResultMapper implements Mapper<DistributedIndexSearchResult> {

	private static final String SEPERATOR = "_";

	private final ParseUtil parseUtil;

	@Inject
	public DistributedIndexSearchResultMapper(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	@Override
	public DistributedIndexSearchResult fromString(final String string) throws MapException {
		try {
			final String[] parts = string.split(SEPERATOR, 2);
			return new DistributedIndexSearchResultImpl(Integer.MAX_VALUE - parseUtil.parseInt(parts[0]), new DistributedIndexPageIdentifier(parts[1]));
		}
		catch (final ParseException e) {
			throw new MapException(e);
		}
	}

	@Override
	public String toString(final DistributedIndexSearchResult distributedIndexSearchResult) {
		return String.format("%010d", Integer.MAX_VALUE - distributedIndexSearchResult.getRating()) + SEPERATOR + distributedIndexSearchResult.getId();
	}
}
