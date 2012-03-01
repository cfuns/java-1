package de.benjaminborbe.dhl.util;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.benjaminborbe.dhl.api.DhlIdentifier;
import de.benjaminborbe.tools.util.RegistryBase;

@Singleton
public class DhlIdentifierRegistry extends RegistryBase<DhlIdentifier> {

	@Inject
	public DhlIdentifierRegistry() {
		// add(new DhlIdentifier(286476016780l, 65185l));
	}

}
