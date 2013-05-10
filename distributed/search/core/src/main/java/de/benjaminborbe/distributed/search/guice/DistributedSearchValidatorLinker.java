package de.benjaminborbe.distributed.search.guice;

import de.benjaminborbe.distributed.search.dao.DistributedSearchPageValidator;
import de.benjaminborbe.lib.validation.ValidatorRegistry;

import javax.inject.Inject;

public class DistributedSearchValidatorLinker {

	@Inject
	public static void link(final ValidatorRegistry validatorRegistry, final DistributedSearchPageValidator a) {
		validatorRegistry.register(a);
	}
}
