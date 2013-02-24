package de.benjaminborbe.poker.gui.util;

import de.benjaminborbe.authentication.api.UserIdentifier;
import de.benjaminborbe.tools.util.ComparatorBase;

public class UserIdentifierComparator extends ComparatorBase<UserIdentifier, String> {

	@Override
	public String getValue(final UserIdentifier o) {
		return o.getId() != null ? o.getId().toLowerCase() : null;
	}

}
