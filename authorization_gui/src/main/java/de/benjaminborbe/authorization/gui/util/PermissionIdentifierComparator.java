package de.benjaminborbe.authorization.gui.util;

import de.benjaminborbe.authorization.api.PermissionIdentifier;
import de.benjaminborbe.tools.util.ComparatorBase;

public class PermissionIdentifierComparator extends ComparatorBase<PermissionIdentifier, String> {

	@Override
	public String getValue(final PermissionIdentifier o) {
		return o.getId() != null ? o.getId().toLowerCase() : null;
	}

}
