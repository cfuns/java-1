package de.benjaminborbe.distributed.search.util;

import com.google.common.base.Predicate;
import de.benjaminborbe.distributed.search.DistributedSearchConstants;

public class MinWordLengthPredicate implements Predicate<String> {

	@Override
	public boolean apply(final String input) {
		return input != null && input.length() >= DistributedSearchConstants.MIN_WORD_LENGTH;
	}

}
