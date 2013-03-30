package de.benjaminborbe.virt.core.guice;

import com.google.inject.Inject;
import de.benjaminborbe.tools.validation.ValidatorRegistry;
import de.benjaminborbe.virt.core.dao.VirtNetworkValidator;

public class VirtValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final VirtNetworkValidator virtNetworkValidator) {
		validatorRegistry.register(virtNetworkValidator);
	}
}
