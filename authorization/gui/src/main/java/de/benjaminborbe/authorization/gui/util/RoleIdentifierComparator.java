package de.benjaminborbe.authorization.gui.util;

import de.benjaminborbe.authorization.api.RoleIdentifier;
import de.benjaminborbe.tools.util.ComparatorBase;

public class RoleIdentifierComparator extends ComparatorBase<RoleIdentifier, String> {

	@Override
	public String getValue(final RoleIdentifier o) {
		return o.getId() != null ? o.getId().toLowerCase() : null;
	}

}
