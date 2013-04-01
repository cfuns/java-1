package de.benjaminborbe.distributed.index.util;

import com.google.inject.Inject;

import de.benjaminborbe.distributed.index.DistributedIndexConstants;
import de.benjaminborbe.distributed.index.api.DistributedIndexSearchResult;
import de.benjaminborbe.tools.mapper.MapException;
import de.benjaminborbe.tools.mapper.Mapper;
import de.benjaminborbe.tools.util.ParseException;
import de.benjaminborbe.tools.util.ParseUtil;

public class DistributedIndexSearchResultMapper implements Mapper<DistributedIndexSearchResult> {

	private final ParseUtil parseUtil;

	@Inject
	public DistributedIndexSearchResultMapper(final ParseUtil parseUtil) {
		this.parseUtil = parseUtil;
	}

	@Override
	public DistributedIndexSearchResult fromString(final String string) throws MapException {
		try {
			final String[] parts = string.split(DistributedIndexConstants.SEPERATOR, 3);
			return new DistributedIndexSearchResultImpl(Integer.MAX_VALUE - parseUtil.parseInt(parts[0]), parts[1], parts[2]);
		}
		catch (final ParseException e) {
			throw new MapException(e);
		}
	}

	@Override
	public String toString(final DistributedIndexSearchResult distributedIndexSearchResult) {
		return String.format("%010d", Integer.MAX_VALUE - distributedIndexSearchResult.getRating()) + DistributedIndexConstants.SEPERATOR + distributedIndexSearchResult.getIndex()
				+ DistributedIndexConstants.SEPERATOR + distributedIndexSearchResult.getId();
	}
}
